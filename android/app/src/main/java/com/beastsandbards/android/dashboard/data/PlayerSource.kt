package com.beastsandbards.android.dashboard.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlayerSource(resources: Resources) {

    companion object {
        private var INSTANCE: PlayerSource? = null
        fun getPlayerSource(resources: Resources) : PlayerSource {
            return synchronized(PlayerSource::class) {
                val newInstance = INSTANCE ?: PlayerSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    // Get player list
    private val initialPlayerList = playerList().sortedBy { it.name }
    private val playerLiveData = MutableLiveData(initialPlayerList)

    fun addPlayer(player: Player) {
        val currentList = playerLiveData.value
        if (currentList == null){
            playerLiveData.postValue(listOf(player))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0,player)
            updatedList.sortBy{ it.name }
            playerLiveData.postValue(updatedList)
        }
    }

    fun removePlayer(player: Player) {
        val currentList = playerLiveData.value
        if (currentList != null){
            val updatedList = currentList.toMutableList()
            updatedList.remove(player)
            playerLiveData.postValue(updatedList)
        }
    }

    fun getPlayerList() : LiveData<List<Player>> {
        return playerLiveData
    }
}
