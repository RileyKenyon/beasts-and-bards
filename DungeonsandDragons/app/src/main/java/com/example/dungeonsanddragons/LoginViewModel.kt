package com.example.dungeonsanddragons

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class LoginViewModel : ViewModel() {

    companion object {
        val TAG = "LoginViewModel"
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val firebaseUserData = FirebaseUserLiveData()
    }
}