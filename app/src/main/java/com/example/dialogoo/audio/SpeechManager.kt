package com.example.dialogoo.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.dialogoo.model.PronunciationFeedback
import com.example.dialogoo.model.WordScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var speechRecognizer: SpeechRecognizer? = null

    private val _isSpeaking = MutableStateFlow<String?>(null)
    val isSpeaking: StateFlow<String?> = _isSpeaking.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _speechRecognitionResult = MutableStateFlow("")
    val speechRecognitionResult: StateFlow<String> = _speechRecognitionResult.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
                tts?.setPitch(1.02f)
            }
        }
        setupUtteranceListener()
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = utteranceId
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = null
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = null
            }
        })
    }

    fun speak(text: String, utteranceId: String = "tts-${System.currentTimeMillis()}", rate: Float = 0.85f, onDone: (() -> Unit)? = null) {
        if (!isTtsReady) return
        tts?.setSpeechRate(rate)
        val params = Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = null
    }

    fun startListening(onResult: (String) -> Unit) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _isListening.value = false
            return
        }

        stopListening()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    _isListening.value = true
                    _speechRecognitionResult.value = ""
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    _isListening.value = false
                }

                override fun onError(error: Int) {
                    _isListening.value = false
                }

                override fun onResults(results: Bundle?) {
                    _isListening.value = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val best = matches?.firstOrNull() ?: ""
                    _speechRecognitionResult.value = best
                    onResult(best)
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val partial = matches?.firstOrNull() ?: ""
                    _speechRecognitionResult.value = partial
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
        _isListening.value = false
    }

    fun evaluatePronunciation(targetLine: String, spokenText: String): PronunciationFeedback {
        val cleanTarget = targetLine.trim().lowercase().replace(Regex("[^a-z0-9\\s']"), "")
        val cleanSpoken = spokenText.trim().lowercase().replace(Regex("[^a-z0-9\\s']"), "")

        val targetWords = cleanTarget.split(Regex("\\s+")).filter { it.isNotBlank() }
        val spokenWords = cleanSpoken.split(Regex("\\s+")).filter { it.isNotBlank() }

        if (targetWords.isEmpty()) {
            return PronunciationFeedback(100, "excellent", emptyList(), "Pronúncia perfeita!")
        }

        val wordScores = mutableListOf<WordScore>()
        var correctCount = 0

        targetWords.forEach { word ->
            val match = spokenWords.any { s ->
                s == word || calculateLevenshteinSimilarity(word, s) >= 0.75
            }
            if (match) {
                correctCount++
                wordScores.add(WordScore(word, true, 95))
            } else {
                wordScores.add(WordScore(word, false, 30))
            }
        }

        val accuracyScore = ((correctCount.toFloat() / targetWords.size) * 100).toInt().coerceIn(10, 100)
        val accuracyCategory = when {
            accuracyScore >= 85 -> "excellent"
            accuracyScore >= 65 -> "good"
            accuracyScore >= 45 -> "average"
            else -> "needs_improvement"
        }

        val verdict = when (accuracyCategory) {
            "excellent" -> "Excelente ritmo e pronúncia nativa! Clareza impecável."
            "good" -> "Muito bom! Frase compreensível com pequenas variações de entonação."
            "average" -> "Bom esforço! Tente articular as palavras destacadas em vermelho mais devagar."
            else -> "Continue praticando! Ouça a referência nativa no botão de áudio e repita."
        }

        return PronunciationFeedback(
            score = accuracyScore,
            accuracy = accuracyCategory,
            words = wordScores,
            verdict = verdict
        )
    }

    private fun calculateLevenshteinSimilarity(s1: String, s2: String): Double {
        val maxLen = maxOf(s1.length, s2.length)
        if (maxLen == 0) return 1.0
        val dist = levenshteinDistance(s1, s2)
        return (maxLen - dist).toDouble() / maxLen
    }

    private fun levenshteinDistance(lhs: CharSequence, rhs: CharSequence): Int {
        val len0 = lhs.length + 1
        val len1 = rhs.length + 1
        var cost = IntArray(len0)
        var newcost = IntArray(len0)
        for (i in 0 until len0) cost[i] = i
        for (j in 1 until len1) {
            newcost[0] = j
            for (i in 1 until len0) {
                val match = if (lhs[i - 1] == rhs[j - 1]) 0 else 1
                val costReplace = cost[i - 1] + match
                val costInsert = cost[i] + 1
                val costDelete = newcost[i - 1] + 1
                newcost[i] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newcost
            newcost = swap
        }
        return cost[len0 - 1]
    }

    fun shutdown() {
        stopSpeaking()
        stopListening()
        tts?.shutdown()
        tts = null
    }
}
