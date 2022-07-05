package com.beastsandbards.android.dashboard.data

import com.beastsandbards.android.FirebaseUserLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class UserSource {
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
    private val firebaseUser = FirebaseUserLiveData().value
    private lateinit var user : User

    // TODO: Find a better way to access firebase rt database
    private val database = Firebase.database
    var ref = database.getReference("users")

    // If already in database, pull the values
    var userData = ref.get().addOnCompleteListener{ data ->
        for (u in  data.result.children){
            val userData = u.value as HashMap<*, *>
            if (userData["uuid"] == firebaseUser?.uid) {
                // TODO: Add game list to user constructor
                user = User(
                    userData["uuid"] as String,
                    userData["name"] as String,
                    userData["active"] as Boolean
                )
                break
            }
        }
        // If not in the database set new credentials
        if (user == null) {
            user = User(
                firebaseUser!!.uid,
                firebaseUser.displayName!!,
                true
            )
        }
    }
}
