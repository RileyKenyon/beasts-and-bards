package com.example.dungeonsanddragons.dashboard.data

data class Player (
    val charId: Long,
    val userId: Long,
    val description: String
)

fun playerList(): List<Player> {
    return listOf(
        Player(
            charId = 0,
            userId = 0,
            description = "Bob"
        ),
        Player(
            charId = 1,
            userId = 0,
            description = "Ted"
        ),
        Player(
            charId = 2,
            userId = 1,
            description = "Aly"
        ),
        Player(
            charId = 3,
            userId = 1,
            description = "Dr. H"
        )
    )
}
