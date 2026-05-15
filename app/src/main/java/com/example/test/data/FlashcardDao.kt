package com.example.test.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: FlashcardSet): Long

    @Update
    suspend fun update(set: FlashcardSet)

    @Delete
    suspend fun delete(set: FlashcardSet)

    @Query("SELECT * FROM flashcard_sets ORDER BY id DESC")
    fun getAll(): Flow<List<FlashcardSet>>
}
