package com.example.dialogoo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialogues")
data class DialogueEntity(
    @PrimaryKey val id: String,
    val title: String,
    val situation: String,
    val level: String,
    val orderIndex: Int,
    val linesJson: String,
    val coverDrawableRes: String,
    val isCustom: Boolean = false,
    val isHidden: Boolean = false
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val userId: String = "local_student",
    val xp: Int = 0,
    val streak: Int = 1,
    val lastActiveDate: String = "",
    val completedDialoguesJson: String = "[]",
    val badgesJson: String = "[]",
    val targetLevel: String = "A1",
    val speechRate: Float = 0.85f,
    val isAdmin: Boolean = false,
    val studentName: String = "Estudante Nativo"
)

@Entity(tableName = "srs_phrases")
data class SrsPhraseEntity(
    @PrimaryKey val phraseId: String,
    val text: String,
    val translation: String,
    val pronunciationGuide: String,
    val lessonTitle: String,
    val level: String,
    val speaker: String,
    val srsLevel: String,
    val consecutiveHits: Int,
    val nextReviewTime: Long,
    val isLearned: Boolean
)
