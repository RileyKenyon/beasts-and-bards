package com.beastsandbards.android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beastsandbards.android.dashboard.data.User
import com.beastsandbards.android.dashboard.data.UserSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Currently not being used
 */
class LoginViewModel (val userSource: UserSource) : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }
//    val userLiveData = FirebaseUserLiveData()
    val userLiveData = UserLiveData()
//    val userLiveData : MutableLiveData<User> by lazy {
//        MutableLiveData<User>()
//    }

//    val userLiveData = userSource.getUser()
//
//
//    var userData : FirebaseUser? = null
//    val userLiveData : LiveData<User>
//        get() {
//            return UserSource(userData)
//        }
//
//    //    var userLiveData = FirebaseUserLiveData()
//    private val firebaseAuth = FirebaseAuth.getInstance().addAuthStateListener {
//        FirebaseAuth.AuthStateListener {
//            userData = it.currentUser
//        }
//    }
}
class LoginViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                userSource = UserSource.getUserSource()
            ) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}

class UserLiveData : LiveData<User?>() {
    companion object {
        const val TAG = "UserData"
    }

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val currentUser = firebaseAuth.currentUser
        value = null
        if (currentUser != null) {
            value = User(
                currentUser.uid.toString(),
                currentUser.displayName.toString(),
                active = true
            )
            Log.d(TAG, value.toString())
        } else {
            Log.d(TAG,"no user")
        }
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
