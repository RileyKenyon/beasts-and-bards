package com.example.dungeonsanddragons.dashboard.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dungeonsanddragons.dashboard.data.Game
import com.example.dungeonsanddragons.dashboard.data.GameSource
import com.example.dungeonsanddragons.dashboard.data.playerList
import java.lang.IllegalArgumentException
import kotlin.random.Random

class GameListViewModel (val dataSource: GameSource): ViewModel() {

    val gameLiveData = dataSource.getGameList()

    // Check if data is present
    fun insertGame(newGame: Game?) {
        if (newGame == null) {
            return
        }
//        val newGame = Game(
//            Random.nextLong(),
//            gameName,
//            playerList(),
//            active = true
//        )
        dataSource.addGame(newGame)
    }
}

class GameListViewModelFactory(private val context: Context?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return GameListViewModel(
                dataSource = GameSource.getGameSource(context?.resources!!)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}