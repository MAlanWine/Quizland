package com.example.test.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {
    var profile by mutableStateOf(
        UserProfile(
            nickname = "Alan",
            bio = "Mobile programming student @ UKM",
            email = "waalanwine@outlook.com"
        )
    )
        private set

    fun updateNickname(value: String) {
        profile = profile.copy(nickname = value)
    }

    fun updateBio(value: String) {
        profile = profile.copy(bio = value)
    }

    fun updateEmail(value: String) {
        profile = profile.copy(email = value)
    }
}
