package com.beastsandbards.android.dashboard.data

import com.google.firebase.database.Exclude

data class Game(
    val id: Long,
    val name: String,
    val participants: List<Player>,
    val active: Boolean
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "participants" to participants,
            "active" to active
        )
    }
}

fun tempGameList() : List<Game> {

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
            active = false
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
            active = false
        ),
        Game(
            id = 4,
            name = "Knights Templar",
            participants = playerList(),
            active = false
        ),
        Game(
            id = 5,
            name = "Bowling for Soup",
            participants = playerList(),
            active = false
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
        )
    )
}
