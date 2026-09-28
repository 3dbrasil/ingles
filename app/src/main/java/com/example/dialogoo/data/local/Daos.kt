package com.example.dialogoo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DialogueDao {
    @Query("SELECT * FROM dialogues WHERE isHidden = 0 ORDER BY orderIndex ASC")
    fun getAllVisibleDialogues(): Flow<List<DialogueEntity>>

    @Query("SELECT * FROM dialogues WHERE id = :id LIMIT 1")
    suspend fun getDialogueById(id: String): DialogueEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(dialogues: List<DialogueEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(dialogue: DialogueEntity)

    @Query("UPDATE dialogues SET isHidden = 1 WHERE id = :id")
    suspend fun hideDialogue(id: String)

    @Query("DELETE FROM dialogues WHERE id = :id")
    suspend fun deleteDialogue(id: String)

    @Query("DELETE FROM dialogues WHERE isCustom = 1")
    suspend fun deleteAllCustom()
}

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    fun getUserStatsFlow(userId: String = "local_student"): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    suspend fun getUserStats(userId: String = "local_student"): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserStats(stats: UserStatsEntity)

    @Query("DELETE FROM user_stats WHERE userId = :userId")
    suspend fun clearStats(userId: String = "local_student")
}

@Dao
interface SrsDao {
    @Query("SELECT * FROM srs_phrases ORDER BY nextReviewTime ASC")
    fun getAllSrsPhrases(): Flow<List<SrsPhraseEntity>>

    @Query("SELECT * FROM srs_phrases WHERE isLearned = 1")
    fun getLearnedPhrases(): Flow<List<SrsPhraseEntity>>

    @Query("SELECT * FROM srs_phrases WHERE phraseId = :id LIMIT 1")
    suspend fun getPhraseById(id: String): SrsPhraseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(phrase: SrsPhraseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(phrases: List<SrsPhraseEntity>)

    @Query("UPDATE srs_phrases SET isLearned = :learned WHERE phraseId = :id")
    suspend fun setLearned(id: String, learned: Boolean)

    @Query("DELETE FROM srs_phrases")
    suspend fun clearAll()
}
