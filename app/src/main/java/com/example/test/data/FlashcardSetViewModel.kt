package com.example.test.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FlashcardSetViewModel : ViewModel() {
    private val _sets = mutableStateListOf(
        FlashcardSet(
            id = 1L,
            title = "Test Flash Cards",
            description = "Sample starter deck",
            author = "you",
            cardCount = 2
        ),
        FlashcardSet(
            id = 2L,
            title = "Quizland basics",
            description = "Intro to the app",
            author = "Quizland",
            cardCount = 11
        )
    )
    val sets: List<FlashcardSet> get() = _sets

    fun addSet(title: String, description: String, author: String, cardCount: Int) {
        _sets.add(
            0,
            FlashcardSet(
                id = System.currentTimeMillis(),
                title = title.trim(),
                description = description.trim(),
                author = author.ifBlank { "you" },
                cardCount = cardCount
            )
        )
    }
}
