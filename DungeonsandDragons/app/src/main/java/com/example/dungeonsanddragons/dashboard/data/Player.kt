package com.example.dungeonsanddragons.dashboard.data

data class Player (
    val charId: Long,
    val userId: Long,
    val name: String
)

fun playerList(): List<Player> {
    return listOf(
        Player(
            charId = 0,
            userId = 0,
            name = "Bob"
        ),
        Player(
            charId = 1,
            userId = 0,
            name = "Ted"
        ),
        Player(
            charId = 2,
            userId = 1,
            name = "Aly"
        ),
        Player(
            charId = 3,
            userId = 1,
            name = "Dr. H"
        )
    )
}
