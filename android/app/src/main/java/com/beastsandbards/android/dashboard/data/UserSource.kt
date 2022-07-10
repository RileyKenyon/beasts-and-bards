package com.beastsandbards.android.dashboard.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beastsandbards.android.FirebaseUserLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class UserSource() {
    companion object {
        const val TAG = "UserSource"
        private var INSTANCE: UserSource? = null
        fun getUserSource() : UserSource {
            return synchronized(UserSource::class){
                val newInstance = INSTANCE ?: UserSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    // TODO: Firebase user data should be fed into constructor
//    val userLiveData = UserLiveData()
//    val userLiveData = MutableLiveData<User>()
//    private fun getUser() : User? {
//        var user : User? = null
//        firebaseUserliveData.value?.let {
////        Firebase.auth.currentUser?.let {
//            user = User(
//                it.uid.toString(),
//                it.displayName!!,
//                true
//            )
//        }
//        return user
//    }
//    fun getUser(): UserLiveData {
//        return userLiveData
//    }

//    val authListener = FirebaseAuth.AuthStateListener() {
//        if (it.currentUser != null) {
//            userLiveData.postValue(it.let {  auth ->
//                auth.currentUser!!.displayName?.let { displayName ->
//                    User(it.uid.toString(),
//                        displayName,
//                        true
//                    )
//                }
//            })
//        }
//    }
//    private val firebaseUser = Firebase.auth.currentUser
//    val firebaseUserLiveData = FirebaseUserLiveData()
//    private var user : User? = null

//    fun getUserData() : LiveData<User> {
//        val user = getUser()
////        if (user != null) {
//            return object: LiveData<User>(user){}
////        }
//    }

    // TODO: Find a better way to access firebase rt database
    private val database = Firebase.database
    var ref = database.getReference("users")

    // If already in database, pull the values
//    private var addUserToDatabase = ref.get().addOnCompleteListener{ data ->
//        for (u in  data.result.children){
//            val userData = u.value as HashMap<*, *>
//            if (userData["uuid"] == userLiveData.value?.uuid) {
//                // TODO: Add game list to user constructor
//                val user = User(
//                    userData["uuid"] as String,
//                    userData["name"] as String,
//                    userData["active"] as Boolean
//                )
//                break
//            }
//        }
//        // If not in the database set new credentials
////        if (user == null) {
////            user = User(
////                getUser()!!.uid,
////                getUser().displayName!!,
////                true
////            )
////        }
//    }

    // Authentication State
//    enum class AuthenticationState {
//        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
//    }
//
//    fun getAuthenticationState() : AuthenticationState {
//        var authState = AuthenticationState.UNAUTHENTICATED
//        if (userLiveData.value != null) {
//            authState = AuthenticationState.AUTHENTICATED
//        }
//        return authState
//    }
}