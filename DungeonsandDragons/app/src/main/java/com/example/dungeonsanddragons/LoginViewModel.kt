package com.example.dungeonsanddragons

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random

/**
 * Currently not being used
 */
class LoginViewModel : ViewModel() {

    companion object {
        val TAG = "LoginViewModel"
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val firebaseUserData = FirebaseUserLiveData()
}