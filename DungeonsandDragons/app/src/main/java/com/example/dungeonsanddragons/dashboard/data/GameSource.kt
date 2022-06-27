package com.example.dungeonsanddragons.dashboard.data

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

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
    var gameList : List<Game> = tempGameList()
    private var gameLiveData = MutableLiveData(gameList)

    // TODO: Clean up listener code - should request games only from player
    private val gameListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.childrenCount > 0) {
                val gameListServer : MutableList<Game> = mutableListOf<Game>()
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
                gameLiveData.postValue(gameListServer.toList())
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
            updatedList.sortBy { it.name }
            updatedList.sortByDescending { it.active }
            gameLiveData.postValue(updatedList)
        }
    }

    fun getNextId() : Int {
        val currentList = gameLiveData.value
        return if (currentList == null){
            0
        } else {
            gameLiveData.value?.size!!
        }

    }

    fun getGameList() : LiveData<List<Game>> {
        // sorting of initial data by active games

        val initialGameList = gameList?.sortedBy {it.name}.sortedBy { !it.active }
        gameLiveData = MutableLiveData(initialGameList)
        return gameLiveData
    }
}