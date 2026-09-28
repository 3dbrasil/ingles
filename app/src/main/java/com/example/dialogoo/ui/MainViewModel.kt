package com.example.dialogoo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dialogoo.ai.AriaTutorService
import com.example.dialogoo.ai.ChatMessage
import com.example.dialogoo.audio.SpeechManager
import com.example.dialogoo.data.ArenaChallenges
import com.example.dialogoo.data.repository.DialogueRepository
import com.example.dialogoo.model.ArenaChallenge
import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.model.DialogueLine
import com.example.dialogoo.model.Level
import com.example.dialogoo.model.PronunciationFeedback
import com.example.dialogoo.model.SrsLevel
import com.example.dialogoo.model.SrsPhrase
import com.example.dialogoo.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    MAP,
    PRACTICE,
    ARENA,
    REPETITION,
    SETTINGS,
    ARIA
}

data class CelebrationState(
    val isVisible: Boolean = false,
    val title: String = "",
    val message: String = "",
    val xpEarned: Int = 0
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DialogueRepository(application)
    val speechManager = SpeechManager(application)
    private val ariaService = AriaTutorService()

    val dialogues: StateFlow<List<Dialogue>> = repository.dialoguesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userStats: StateFlow<UserStats> = repository.userStatsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats())

    val srsPhrases: StateFlow<List<SrsPhrase>> = repository.srsPhrasesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(Screen.MAP)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedDialogue = MutableStateFlow<Dialogue?>(null)
    val selectedDialogue: StateFlow<Dialogue?> = _selectedDialogue.asStateFlow()

    private val _celebration = MutableStateFlow(CelebrationState())
    val celebration: StateFlow<CelebrationState> = _celebration.asStateFlow()

    // Arena challenge state
    private val _currentArenaChallenge = MutableStateFlow<ArenaChallenge?>(null)
    val currentArenaChallenge: StateFlow<ArenaChallenge?> = _currentArenaChallenge.asStateFlow()

    private val _arenaFeedback = MutableStateFlow<PronunciationFeedback?>(null)
    val arenaFeedback: StateFlow<PronunciationFeedback?> = _arenaFeedback.asStateFlow()

    private val _arenaClaimed = MutableStateFlow(false)
    val arenaClaimed: StateFlow<Boolean> = _arenaClaimed.asStateFlow()

    // Aria Chat state
    private val _ariaMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val ariaMessages: StateFlow<List<ChatMessage>> = _ariaMessages.asStateFlow()

    init {
        generateNewArenaChallenge()
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectDialogueForPractice(dialogue: Dialogue) {
        _selectedDialogue.value = dialogue
        _currentScreen.value = Screen.PRACTICE
    }

    fun openAriaChat(dialogue: Dialogue? = null) {
        val target = dialogue ?: _selectedDialogue.value ?: dialogues.value.firstOrNull()
        if (target != null) {
            _selectedDialogue.value = target
            val greeting = ariaService.generateInitialGreeting(target)
            _ariaMessages.value = listOf(greeting)
            speechManager.speak(greeting.text, rate = userStats.value.speechRate)
        }
        _currentScreen.value = Screen.ARIA
    }

    fun sendStudentMessageToAria(studentText: String) {
        val target = _selectedDialogue.value ?: dialogues.value.firstOrNull() ?: return
        val studentMsg = ChatMessage(
            id = "msg-${System.currentTimeMillis()}",
            sender = "You",
            text = studentText
        )
        val updated = _ariaMessages.value.toMutableList().apply { add(studentMsg) }
        _ariaMessages.value = updated

        viewModelScope.launch {
            val ariaReply = ariaService.respondToStudent(target, studentText)
            _ariaMessages.value = _ariaMessages.value + ariaReply
            speechManager.speak(ariaReply.text, rate = userStats.value.speechRate)
        }
    }

    fun completeDialoguePractice(dialogueId: String, scoreAvg: Int) {
        val xpEarned = if (scoreAvg >= 80) 40 else if (scoreAvg >= 60) 30 else 20
        viewModelScope.launch {
            repository.completeDialogue(dialogueId, xpEarned)
            _celebration.value = CelebrationState(
                isVisible = true,
                title = "Lição Concluída!",
                message = "Excelente pronúncia nativa e dedicação!",
                xpEarned = xpEarned
            )
        }
    }

    fun dismissCelebration() {
        _celebration.value = CelebrationState(isVisible = false)
        _currentScreen.value = Screen.MAP
    }

    fun speakLine(line: DialogueLine) {
        speechManager.speak(line.text, utteranceId = line.id, rate = userStats.value.speechRate)
    }

    fun speakText(text: String) {
        speechManager.speak(text, rate = userStats.value.speechRate)
    }

    fun generateNewArenaChallenge() {
        _currentArenaChallenge.value = ArenaChallenges.pool.random()
        _arenaFeedback.value = null
        _arenaClaimed.value = false
    }

    fun evaluateArenaSpeech(spokenText: String) {
        val challenge = _currentArenaChallenge.value ?: return
        val feedback = speechManager.evaluatePronunciation(challenge.targetLine, spokenText)
        _arenaFeedback.value = feedback
    }

    fun claimArenaXp() {
        if (_arenaClaimed.value) return
        _arenaClaimed.value = true
        viewModelScope.launch {
            repository.addXp(20)
            _celebration.value = CelebrationState(
                isVisible = true,
                title = "Desafio Concluído!",
                message = "Você demonstrou precisão fonética na Arena!",
                xpEarned = 20
            )
        }
    }

    fun updateSrsGrade(phraseId: String, newLevel: SrsLevel, isCorrect: Boolean) {
        viewModelScope.launch {
            repository.updateSrsPhrase(phraseId, newLevel, isCorrect)
            if (isCorrect) repository.addXp(5)
        }
    }

    fun markPhraseLearned(phraseId: String, learned: Boolean) {
        viewModelScope.launch {
            repository.markPhraseLearned(phraseId, learned)
        }
    }

    fun setSpeechRate(rate: Float) {
        viewModelScope.launch {
            repository.setSpeechRate(rate)
        }
    }

    fun toggleAdminRole() {
        viewModelScope.launch {
            repository.toggleAdminRole()
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
            _celebration.value = CelebrationState(
                isVisible = true,
                title = "Progresso Reiniciado",
                message = "Sua jornada de fluência foi resetada para o início.",
                xpEarned = 0
            )
        }
    }

    fun deleteDialogue(id: String) {
        viewModelScope.launch {
            repository.deleteDialogue(id, userStats.value.isAdmin)
        }
    }

    fun importCustomDialogue(title: String, situation: String, level: Level, linesText: String) {
        val lines = linesText.split("\n")
            .filter { it.isNotBlank() }
            .mapIndexed { idx, raw ->
                val parts = raw.split("|").map { it.trim() }
                val speaker = if (parts.size >= 2) parts[0] else if (idx % 2 == 0) "Speaker" else "You (Student)"
                val text = if (parts.size >= 2) parts[1] else parts[0]
                val translation = if (parts.size >= 3) parts[2] else ""
                DialogueLine(
                    id = "custom-line-${System.currentTimeMillis()}-$idx",
                    speaker = speaker,
                    text = text,
                    translation = translation,
                    pronunciationGuide = text.lowercase().replace("you", "iu").replace("hello", "he-lou")
                )
            }

        val newDialogue = Dialogue(
            id = "custom-${System.currentTimeMillis()}",
            title = title.ifBlank { "Novo Diálogo Importado" },
            situation = situation.ifBlank { "Prática de conversação customizada" },
            level = level,
            order = (dialogues.value.maxOfOrNull { it.order } ?: 0) + 1,
            lines = lines,
            coverDrawableRes = "cover_travel_conversation",
            isCustom = true
        )

        viewModelScope.launch {
            repository.importDialogues(listOf(newDialogue))
            _celebration.value = CelebrationState(
                isVisible = true,
                title = "Diálogo Importado!",
                message = "'${newDialogue.title}' foi adicionado com ${lines.size} falas.",
                xpEarned = 15
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.shutdown()
    }
}
