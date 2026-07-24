package com.bitcoingame.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "players")
data class Player(
    @Id
    val id: UUID = UUID.randomUUID(),

    val name: String,

    var currentPhase: Int = 1,

    var barterAttempts: Int = 0,

    var goldCoins: Int = 10,

    var kingMints: Int = 0,

    var bitcoins: Int = 0,

    var miningComplete: Boolean = false,

    var gameComplete: Boolean = false,

    var mineCount : Int = 0
)
