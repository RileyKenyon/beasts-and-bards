package com.example.dungeonsanddragons.dashboard.data

data class Game(
    val id: Long,
    val name: String,
    val participants: List<Player>,
    val active: Boolean
)

fun gameList() : List<Game> {
    return listOf(
        Game(
            id = 0,
            name = "Fun Bunch",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 1,
            name = "Special K",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 2,
            name = "Gang of Four",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 3,
            name = "Dun-Runners",
            participants = playerList(),
            active = true
        )
    )
}
