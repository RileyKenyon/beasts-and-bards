package com.example.dungeonsanddragons.dashboard.model

import android.content.Context
import android.util.Log
import android.widget.Filter
import androidx.lifecycle.*
import com.example.dungeonsanddragons.dashboard.data.Player
import com.example.dungeonsanddragons.dashboard.data.PlayerSource
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.random.Random

class PlayerListViewModel (val dataSource: PlayerSource) : ViewModel() {
    val fullPlayerList = dataSource.getPlayerList()
    val _playerLiveData = MutableLiveData<List<Player>>(fullPlayerList.value)
    val playerLiveData : LiveData<List<Player>>
        get() = _playerLiveData
    val newPlayerLiveData = MutableLiveData<List<Player>>()

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

    // Add players to grouping
    fun addPlayerToGroup(player: Player) {
        val currentList = newPlayerLiveData.value
        if (currentList == null){
            newPlayerLiveData.postValue(listOf(player))
        } else if (currentList.contains(player)) {
            return
        }
        else {
            val updatedList = currentList.toMutableList()
            updatedList.add(player)
            newPlayerLiveData.postValue(updatedList)
        }
    }

    // Remove Player from grouping
    fun removePlayerFromGroup(player: Player) {
        val currentList = newPlayerLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(player)
            newPlayerLiveData.postValue(updatedList)
        }
    }

    fun removePlayer(player : Player) {
        // Loop through data and remove from list if matches
        playerLiveData.value?.forEach { playerId ->
            if (playerId.userId == player.userId){
                dataSource.removePlayer(player)
            }
        }

    }

    fun updateFilter(s: CharSequence){
        _playerLiveData.value = fullPlayerList.value?.filter { player ->
            player.name.lowercase().startsWith(s.toString().lowercase())
        }
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