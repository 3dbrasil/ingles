package com.example.dialogoo.data.repository

import android.content.Context
import com.example.dialogoo.data.DefaultDialogues
import com.example.dialogoo.data.local.DialogooDatabase
import com.example.dialogoo.data.local.DialogueEntity
import com.example.dialogoo.data.local.SrsPhraseEntity
import com.example.dialogoo.data.local.UserStatsEntity
import com.example.dialogoo.model.Badge
import com.example.dialogoo.model.Dialogue
import com.example.dialogoo.model.DialogueLine
import com.example.dialogoo.model.Level
import com.example.dialogoo.model.SrsLevel
import com.example.dialogoo.model.SrsPhrase
import com.example.dialogoo.model.UserStats
import com.example.dialogoo.model.VocabularyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DialogueRepository(context: Context) {
    private val db = DialogooDatabase.getInstance(context)
    private val dialogueDao = db.dialogueDao()
    private val userStatsDao = db.userStatsDao()
    private val srsDao = db.srsDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Local student/admin access is read before online auth on app startup
        scope.launch {
            initDefaultDataIfEmpty()
        }
    }

    private suspend fun initDefaultDataIfEmpty() {
        val existingStats = userStatsDao.getUserStats("local_student")
        if (existingStats == null) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            userStatsDao.upsertUserStats(
                UserStatsEntity(
                    userId = "local_student",
                    xp = 50,
                    streak = 1,
                    lastActiveDate = today,
                    completedDialoguesJson = "[]",
                    badgesJson = JSONArray().put(
                        JSONObject().apply {
                            put("id", "first-step")
                            put("title", "Primeira Braçada")
                            put("description", "Iniciou sua jornada oceânica no Dialogoo")
                            put("iconEmoji", "🌊")
                            put("unlockedAt", System.currentTimeMillis())
                        }
                    ).toString(),
                    targetLevel = "A1",
                    speechRate = 0.85f,
                    isAdmin = false,
                    studentName = "Estudante Nativo"
                )
            )
        }

        // Prepopulate default dialogues if none exist
        val entities = DefaultDialogues.list.map { d ->
            DialogueEntity(
                id = d.id,
                title = d.title,
                situation = d.situation,
                level = d.level.code,
                orderIndex = d.order,
                linesJson = serializeLines(d.lines),
                coverDrawableRes = d.coverDrawableRes,
                isCustom = false,
                isHidden = false
            )
        }
        dialogueDao.insertOrUpdateAll(entities)

        // Seed initial SRS phrases from default dialogues
        val srsEntities = DefaultDialogues.list.flatMap { d ->
            d.lines.map { l ->
                SrsPhraseEntity(
                    phraseId = l.id,
                    text = l.text,
                    translation = l.translation,
                    pronunciationGuide = l.pronunciationGuide,
                    lessonTitle = d.title,
                    level = d.level.code,
                    speaker = l.speaker,
                    srsLevel = SrsLevel.MEDIUM.name,
                    consecutiveHits = 0,
                    nextReviewTime = System.currentTimeMillis(),
                    isLearned = false
                )
            }
        }
        srsDao.insertOrUpdateAll(srsEntities)
    }

    val dialoguesFlow: Flow<List<Dialogue>> = dialogueDao.getAllVisibleDialogues().map { list ->
        list.map { entity ->
            Dialogue(
                id = entity.id,
                title = entity.title,
                situation = entity.situation,
                level = Level.fromString(entity.level),
                order = entity.orderIndex,
                lines = deserializeLines(entity.linesJson),
                coverDrawableRes = entity.coverDrawableRes,
                isCustom = entity.isCustom
            )
        }
    }

    val userStatsFlow: Flow<UserStats> = userStatsDao.getUserStatsFlow("local_student").map { entity ->
        if (entity == null) {
            UserStats()
        } else {
            UserStats(
                xp = entity.xp,
                streak = entity.streak,
                lastActiveDate = entity.lastActiveDate,
                completedDialogues = parseStringList(entity.completedDialoguesJson),
                badges = parseBadges(entity.badgesJson),
                targetLevel = Level.fromString(entity.targetLevel),
                speechRate = entity.speechRate,
                isAdmin = entity.isAdmin,
                studentName = entity.studentName
            )
        }
    }

    val srsPhrasesFlow: Flow<List<SrsPhrase>> = srsDao.getAllSrsPhrases().map { list ->
        list.map { e ->
            SrsPhrase(
                id = e.phraseId,
                text = e.text,
                translation = e.translation,
                pronunciationGuide = e.pronunciationGuide,
                lessonTitle = e.lessonTitle,
                level = Level.fromString(e.level),
                speaker = e.speaker,
                srsLevel = SrsLevel.fromString(e.srsLevel),
                consecutiveHits = e.consecutiveHits,
                nextReviewTime = e.nextReviewTime,
                isLearned = e.isLearned
            )
        }
    }

    suspend fun addXp(amount: Int) {
        val current = userStatsDao.getUserStats("local_student") ?: return
        val newXp = current.xp + amount
        val badges = parseBadges(current.badgesJson).toMutableList()

        if (newXp >= 100 && badges.none { it.id == "xp-100" }) {
            badges.add(
                Badge(
                    id = "xp-100",
                    title = "Centurião",
                    description = "Alcançou 100 pontos de experiência",
                    iconEmoji = "⚡"
                )
            )
        }
        if (newXp >= 250 && badges.none { it.id == "xp-250" }) {
            badges.add(
                Badge(
                    id = "xp-250",
                    title = "Mestre das Frases",
                    description = "Alcançou 250 pontos de experiência",
                    iconEmoji = "🏆"
                )
            )
        }

        userStatsDao.upsertUserStats(
            current.copy(
                xp = newXp,
                badgesJson = serializeBadges(badges)
            )
        )
    }

    suspend fun completeDialogue(dialogueId: String, earnedXp: Int) {
        val current = userStatsDao.getUserStats("local_student") ?: return
        val completed = parseStringList(current.completedDialoguesJson).toMutableList()
        if (!completed.contains(dialogueId)) {
            completed.add(dialogueId)
        }

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val streak = if (current.lastActiveDate.isNotEmpty() && current.lastActiveDate != today) {
            current.streak + 1
        } else {
            current.streak
        }

        val badges = parseBadges(current.badgesJson).toMutableList()
        if (completed.size >= 1 && badges.none { it.id == "first-complete" }) {
            badges.add(
                Badge(
                    id = "first-complete",
                    title = "Primeira Conversa",
                    description = "Concluiu seu primeiro diálogo com sucesso!",
                    iconEmoji = "🗣️"
                )
            )
        }
        if (completed.size >= 5 && badges.none { it.id == "five-complete" }) {
            badges.add(
                Badge(
                    id = "five-complete",
                    title = "Poliglota Oceânico",
                    description = "Completou 5 diálogos completos!",
                    iconEmoji = "🐬"
                )
            )
        }

        userStatsDao.upsertUserStats(
            current.copy(
                xp = current.xp + earnedXp,
                streak = streak,
                lastActiveDate = today,
                completedDialoguesJson = JSONArray(completed).toString(),
                badgesJson = serializeBadges(badges)
            )
        )
    }

    suspend fun updateSrsPhrase(phraseId: String, newLevel: SrsLevel, isCorrect: Boolean) {
        val existing = srsDao.getPhraseById(phraseId) ?: return
        val newHits = if (isCorrect) existing.consecutiveHits + 1 else 0
        val intervalMs = when (newLevel) {
            SrsLevel.EASY -> 24 * 60 * 60 * 1000L * (newHits.coerceAtLeast(1))
            SrsLevel.MEDIUM -> 6 * 60 * 60 * 1000L * (newHits.coerceAtLeast(1))
            SrsLevel.HARD -> 2 * 60 * 60 * 1000L
        }
        srsDao.insertOrUpdate(
            existing.copy(
                srsLevel = newLevel.name,
                consecutiveHits = newHits,
                nextReviewTime = System.currentTimeMillis() + intervalMs,
                isLearned = newHits >= 3
            )
        )
    }

    suspend fun markPhraseLearned(phraseId: String, learned: Boolean) {
        srsDao.setLearned(phraseId, learned)
    }

    suspend fun setSpeechRate(rate: Float) {
        val current = userStatsDao.getUserStats("local_student") ?: return
        userStatsDao.upsertUserStats(current.copy(speechRate = rate))
    }

    suspend fun toggleAdminRole() {
        val current = userStatsDao.getUserStats("local_student") ?: return
        userStatsDao.upsertUserStats(
            current.copy(
                isAdmin = !current.isAdmin,
                studentName = if (!current.isAdmin) "Administrador Dialogoo" else "Estudante Nativo"
            )
        )
    }

    suspend fun resetAllProgress() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        userStatsDao.upsertUserStats(
            UserStatsEntity(
                userId = "local_student",
                xp = 0,
                streak = 1,
                lastActiveDate = today,
                completedDialoguesJson = "[]",
                badgesJson = "[]",
                targetLevel = "A1",
                speechRate = 0.85f,
                isAdmin = false,
                studentName = "Estudante Nativo"
            )
        )
        srsDao.clearAll()
        initDefaultDataIfEmpty()
    }

    suspend fun deleteDialogue(id: String, isAdmin: Boolean) {
        if (isAdmin) {
            dialogueDao.deleteDialogue(id)
        } else {
            dialogueDao.hideDialogue(id)
        }
    }

    suspend fun importDialogues(dialogues: List<Dialogue>) {
        val entities = dialogues.map { d ->
            DialogueEntity(
                id = d.id,
                title = d.title,
                situation = d.situation,
                level = d.level.code,
                orderIndex = d.order,
                linesJson = serializeLines(d.lines),
                coverDrawableRes = if (d.coverDrawableRes.isNotEmpty()) d.coverDrawableRes else "cover_travel_conversation",
                isCustom = true,
                isHidden = false
            )
        }
        dialogueDao.insertOrUpdateAll(entities)

        val srsEntities = dialogues.flatMap { d ->
            d.lines.map { l ->
                SrsPhraseEntity(
                    phraseId = l.id,
                    text = l.text,
                    translation = l.translation,
                    pronunciationGuide = l.pronunciationGuide,
                    lessonTitle = d.title,
                    level = d.level.code,
                    speaker = l.speaker,
                    srsLevel = SrsLevel.MEDIUM.name,
                    consecutiveHits = 0,
                    nextReviewTime = System.currentTimeMillis(),
                    isLearned = false
                )
            }
        }
        srsDao.insertOrUpdateAll(srsEntities)
    }

    // JSON serialization helpers
    private fun serializeLines(lines: List<DialogueLine>): String {
        val arr = JSONArray()
        lines.forEach { l ->
            val obj = JSONObject()
            obj.put("id", l.id)
            obj.put("speaker", l.speaker)
            obj.put("text", l.text)
            obj.put("translation", l.translation)
            obj.put("pronunciationGuide", l.pronunciationGuide)
            val vocabArr = JSONArray()
            l.keyVocabulary.forEach { v ->
                val vObj = JSONObject()
                vObj.put("word", v.word)
                vObj.put("translation", v.translation)
                vocabArr.put(vObj)
            }
            obj.put("keyVocabulary", vocabArr)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserializeLines(json: String): List<DialogueLine> {
        val result = mutableListOf<DialogueLine>()
        try {
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val vocabList = mutableListOf<VocabularyItem>()
                val vocabArr = obj.optJSONArray("keyVocabulary")
                if (vocabArr != null) {
                    for (j in 0 until vocabArr.length()) {
                        val vObj = vocabArr.getJSONObject(j)
                        vocabList.add(
                            VocabularyItem(
                                word = vObj.optString("word", ""),
                                translation = vObj.optString("translation", "")
                            )
                        )
                    }
                }
                result.add(
                    DialogueLine(
                        id = obj.optString("id", "line-$i"),
                        speaker = obj.optString("speaker", "You (Student)"),
                        text = obj.optString("text", ""),
                        translation = obj.optString("translation", ""),
                        pronunciationGuide = obj.optString("pronunciationGuide", ""),
                        keyVocabulary = vocabList
                    )
                )
            }
        } catch (_: Exception) {
        }
        return result
    }

    private fun parseStringList(json: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                result.add(arr.getString(i))
            }
        } catch (_: Exception) {
        }
        return result
    }

    private fun serializeBadges(badges: List<Badge>): String {
        val arr = JSONArray()
        badges.forEach { b ->
            val obj = JSONObject()
            obj.put("id", b.id)
            obj.put("title", b.title)
            obj.put("description", b.description)
            obj.put("iconEmoji", b.iconEmoji)
            obj.put("unlockedAt", b.unlockedAt)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun parseBadges(json: String): List<Badge> {
        val result = mutableListOf<Badge>()
        try {
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                result.add(
                    Badge(
                        id = obj.optString("id", ""),
                        title = obj.optString("title", ""),
                        description = obj.optString("description", ""),
                        iconEmoji = obj.optString("iconEmoji", "🏆"),
                        unlockedAt = obj.optLong("unlockedAt", System.currentTimeMillis())
                    )
                )
            }
        } catch (_: Exception) {
        }
        return result
    }
}
