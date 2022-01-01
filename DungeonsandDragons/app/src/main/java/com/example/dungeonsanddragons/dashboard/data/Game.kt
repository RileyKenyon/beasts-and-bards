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
        ),
        Game(
            id = 4,
            name = "Knights Templar",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 5,
            name = "Bowling for Soup",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 6,
            name = "Sno white and 7 Dwarfs",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 7,
            name = "Backstreet Boyz",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 8,
            name = "Fish",
            participants = playerList(),
            active = true
        ),
        Game(
            id = 9,
            name = "Beasts and Bards",
            participants = playerList(),
            active = true
        )
    )
}
