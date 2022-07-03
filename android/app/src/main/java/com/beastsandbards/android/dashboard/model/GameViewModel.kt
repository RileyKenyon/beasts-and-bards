package com.beastsandbards.android.dashboard.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beastsandbards.android.dashboard.data.Game
import com.beastsandbards.android.dashboard.data.GameSource
import com.google.firebase.auth.FirebaseUser
import java.lang.IllegalArgumentException

class GameListViewModel (val dataSource: GameSource): ViewModel() {

    val gameLiveData = dataSource.getGameList()

    // Check if data is present
    fun insertGame(newGame: Game?) {
        if (newGame == null) {
            return
        }
        dataSource.addGame(newGame)
    }
}

class GameListViewModelFactory(private val user: FirebaseUser?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return GameListViewModel(
                dataSource = GameSource.getGameSource(user!!)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
