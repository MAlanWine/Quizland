package com.example.test.data

import kotlinx.coroutines.flow.Flow

class FlashcardRepository(private val dao: FlashcardDao) {

    val allSets: Flow<List<FlashcardSet>> = dao.getAll()

    suspend fun add(set: FlashcardSet): Long = dao.insert(set)

    suspend fun update(set: FlashcardSet) = dao.update(set)

    suspend fun delete(set: FlashcardSet) = dao.delete(set)
}
