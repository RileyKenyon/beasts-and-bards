package com.example.dungeonsanddragons

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.LiveData

class FirebaseUserLiveData : LiveData<FirebaseUser>() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
        if (value != null) {
            Log.d("FirebaseData", value.toString())
        } else {
            Log.d("FirebaseData","no user")
        }
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
