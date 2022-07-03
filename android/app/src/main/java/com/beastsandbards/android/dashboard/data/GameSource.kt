package com.beastsandbards.android.dashboard.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO : Change this interface to be a Player instead
class GameSource(user: FirebaseUser) {

    companion object {
        const val TAG = "GameSource"
        private var INSTANCE: GameSource? = null
        fun getGameSource(user: FirebaseUser) : GameSource {
            return synchronized(GameSource::class){
                val newInstance = INSTANCE ?: GameSource(user)
                INSTANCE = newInstance
                newInstance
            }
        }
    }


    private val database = Firebase.database
    var ref = database.getReference("games")
    var gameLiveData = MutableLiveData<List<Game>?>()
    var gameData = ref.get().addOnCompleteListener{ data ->
        val gameListServer : MutableList<Game> = mutableListOf<Game>()
        for (game in  data.result.children){
            val gameData = game.value as HashMap<*, *>
            val g = Game(
                gameData["id"] as Long,
                gameData["name"].toString(),
                listOf<Player>(),
                gameData["active"] as Boolean
            )
            gameListServer.add(g)
        }
        val localList = gameListServer.sortedBy { it.name.lowercase() }.sortedByDescending { it.active }
        gameLiveData.postValue(localList)
    }

    // TODO: Clean up listener code - should request games only from player
    private val gameListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.childrenCount > 0) {
                var gameListServer : MutableList<Game> = mutableListOf<Game>()
                for (game in snapshot.children) {
                    val gameData = game.value as HashMap<*, *>
                    val g = Game(
                        gameData["id"] as Long,
                        gameData["name"].toString(),
                        listOf<Player>(),
                        gameData["active"] as Boolean
                    )
                    gameListServer.add(g)
                    Log.d(TAG, g.toString())
                }
                // TODO: Reduce the number of times this sorting is called
                val localList = gameListServer.sortedBy { it.name.lowercase() }.sortedByDescending { it.active }
                Log.d(TAG,localList.toString())
                gameLiveData.postValue(localList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled", error.toException())
        }
    }

    // Attach callback for data updates
    val listener = ref.addValueEventListener(gameListener)

    fun addGame(game: Game) {
        val currentList = gameLiveData.value
        if (currentList == null){
            // If empty list, add the passed-in game
            gameLiveData.postValue(listOf(game))
        } else {
            // Add passed-in game to top of list
            val updatedList = currentList.toMutableList()
            updatedList.add(0,game)
            // sort list by active status
            updatedList.sortBy { it.name.lowercase() }
            updatedList.sortByDescending { it.active }
            gameLiveData.postValue(updatedList)
        }

        // Post to the rt database
        val uid = game.name.let { name ->
            name.apply { lowercase() }
                .apply { trim() }
                .apply { replace("\\s".toRegex(),"") }
        }
        val gameValues = game.toMap().toMutableMap()
        // TODO: Add a loop for participants to create the nested map
        gameValues["participants"] = null
        val childUpdates = hashMapOf<String, Any?>(
            "/$uid" to gameValues
        )
        ref.updateChildren(childUpdates)
    }

    fun getNextId() : Int {
        val currentList = gameLiveData.value
        return if (currentList == null){
            0
        } else {
            gameLiveData.value?.size!!
        }

    }

    fun getGameList() : MutableLiveData<List<Game>?> {
        // sorting of initial data by active games
        val initialGameList = gameLiveData.value?.sortedBy {it.name.lowercase()}?.sortedByDescending {it.active}
        gameLiveData.postValue(initialGameList)
        return gameLiveData
    }
}
