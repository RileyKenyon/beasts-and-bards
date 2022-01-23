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
//    var _playerLiveData = MutableLiveData<List<Player>>(dataSource.getPlayerList().value)

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

//    fun getFilteredPlayerList(filter: Filter) {
//        return performFiltering() playerLiveData
//    }
//    init {
//        viewModelScope.launch {
//            filter.collect { newFilter ->
//                savedStateHandle.set(newFilter)
//
//            }
//        }
//    }
//    init {
//        _playerLiveData.value = dataSource.getPlayerList().value
//    }

    fun updateFilter(s: CharSequence){
        _playerLiveData.value = fullPlayerList.value?.filter { player ->
            player.name.startsWith(s)
        }

//            Transformations.switchMap(_playerLiveData.value){
//            val filteredPlayerList = MutableLiveData<List<Player>>()
//            filteredPlayerList.value = it?.filter { player ->
//                player.name.startsWith(s)
//            }
//            filteredPlayerList
//        }
//        Log.d("PLAYERVIEWMODEL","Working!")
//        playerLiveData.value?.forEach { player ->
//            Log.d("PLAYERVIEWMODEL",player.name.toString())
//        }
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