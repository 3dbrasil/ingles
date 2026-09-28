package com.example.dialogoo.model

enum class Level(val code: String, val title: String, val subtitle: String, val tagline: String) {
    A1("A1", "Iniciante", "Fundamentos do Oceano", "Saudações, apresentações e estruturas essenciais."),
    A2("A2", "Básico+", "Correntes Superficiais", "Expandindo vocabulário para situações cotidianas."),
    B1("B1", "Intermediário", "Mergulho Intermediário", "Onde a gramática se torna instinto — opiniões e planos."),
    B2("B2", "Avançado-", "Águas Profundas", "Argumentação, nuances e fluência profissional."),
    C1("C1", "Avançado", "Correntes Abissais", "Sutilezas idiomáticas e pensamento crítico."),
    C2("C2", "Maestria", "Domínio Oceânico", "Precisão nativa em qualquer contexto.");

    companion object {
        fun fromString(value: String): Level =
            entries.find { it.code.equals(value, ignoreCase = true) } ?: A1
    }
}

enum class SrsLevel(val label: String, val colorHex: Long) {
    EASY("Fácil", 0xFF10B981),
    MEDIUM("Médio", 0xFFF59E0B),
    HARD("Difícil", 0xFFEF4444);

    companion object {
        fun fromString(value: String): SrsLevel =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
    }
}

data class VocabularyItem(
    val word: String,
    val translation: String
)

data class DialogueLine(
    val id: String,
    val speaker: String,
    val text: String,
    val translation: String,
    val pronunciationGuide: String = "",
    val keyVocabulary: List<VocabularyItem> = emptyList()
) {
    val isStudent: Boolean
        get() = speaker.contains("you", ignoreCase = true) ||
                speaker.contains("student", ignoreCase = true) ||
                speaker.contains("aluno", ignoreCase = true) ||
                speaker.contains("você", ignoreCase = true)
}

data class Dialogue(
    val id: String,
    val title: String,
    val situation: String,
    val level: Level,
    val order: Int,
    val lines: List<DialogueLine>,
    val coverDrawableRes: String = "",
    val isCustom: Boolean = false
)

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

data class UserStats(
    val xp: Int = 0,
    val streak: Int = 1,
    val lastActiveDate: String = "",
    val completedDialogues: List<String> = emptyList(),
    val badges: List<Badge> = emptyList(),
    val targetLevel: Level = Level.A1,
    val speechRate: Float = 0.85f,
    val isAdmin: Boolean = false,
    val studentName: String = "Estudante Nativo"
)

data class WordScore(
    val word: String,
    val isCorrect: Boolean,
    val score: Int
)

data class PronunciationFeedback(
    val score: Int,
    val accuracy: String,
    val words: List<WordScore>,
    val verdict: String
)

data class ArenaChallenge(
    val id: String,
    val title: String,
    val situation: String,
    val targetLine: String,
    val translation: String,
    val level: Level,
    val coverDrawableRes: String = ""
)

data class SrsPhrase(
    val id: String,
    val text: String,
    val translation: String,
    val pronunciationGuide: String,
    val lessonTitle: String,
    val level: Level,
    val speaker: String,
    val srsLevel: SrsLevel = SrsLevel.MEDIUM,
    val consecutiveHits: Int = 0,
    val nextReviewTime: Long = 0L,
    val isLearned: Boolean = false
)
