package com.beastsandbards.android

import androidx.lifecycle.ViewModel

/**
 * Currently not being used
 */
class LoginViewModel : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val firebaseUserData = FirebaseUserLiveData()
}
