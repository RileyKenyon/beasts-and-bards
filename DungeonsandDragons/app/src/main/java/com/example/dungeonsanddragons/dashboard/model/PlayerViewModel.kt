package com.example.dungeonsanddragons.dashboard.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dungeonsanddragons.dashboard.data.Player
import com.example.dungeonsanddragons.dashboard.data.PlayerSource
import java.lang.IllegalArgumentException
import kotlin.random.Random

class PlayerListViewModel (val dataSource: PlayerSource) : ViewModel() {
    val playerLiveData = dataSource.getPlayerList()

    // check if data is present
    fun insertPlayer(playerName: String?) {
        if (playerName == null){
            return
        }
        // TODO: Add in userId from FirebaseData
        val newPlayer = Player(
            charId = Random.nextLong(),
            userId = Random.nextLong(),
            name = playerName
        )
        dataSource.addPlayer(newPlayer)
    }
}

class PlayerListViewModelFactory(private val context: Context?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return PlayerListViewModel(
                dataSource = PlayerSource.getPlayerSource(context?.resources!!)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}