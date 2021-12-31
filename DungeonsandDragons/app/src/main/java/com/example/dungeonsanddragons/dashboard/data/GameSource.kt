package com.example.dungeonsanddragons.dashboard.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameSource(resources: Resources) {

    companion object {
        private var INSTANCE: GameSource? = null
        fun getGameSource(resources: Resources) : GameSource {
            return synchronized(GameSource::class){
                val newInstance = INSTANCE ?: GameSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
    private val initialGameList = gameList()
    private val gameLiveData = MutableLiveData(initialGameList)

    fun addGame(game: Game) {
        val currentList = gameLiveData.value
        if (currentList == null){
            // If empty list, add the passed-in game
            gameLiveData.postValue(listOf(game))
        } else {
            // Add passed-in game to top of list
            val updatedList = currentList.toMutableList()
            updatedList.add(0,game)
            gameLiveData.postValue(updatedList)
        }
    }

    fun getGameList() : LiveData<List<Game>> {
        return gameLiveData
    }
}