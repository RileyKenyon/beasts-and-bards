package com.beastsandbards.android.dashboard.data

import com.google.firebase.database.Exclude

// TODO: Add game list to user
data class User(
    val uuid: String,
    val name: String,
    val active: Boolean
//    val games : List<Long>
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uuid" to uuid,
            "name" to name,
            "active" to active,
//            "games" to games
        )
    }
}