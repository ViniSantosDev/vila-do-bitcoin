package com.bitcoingame.model

import jakarta.persistence.*

@Entity
@Table(name = "players")
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    var currentPhase: Int = 1,

    var barterAttempts: Int = 0,

    var goldCoins: Int = 10,

    var kingMints: Int = 0,

    var bitcoins: Int = 0,

    var miningComplete: Boolean = false,

    var gameComplete: Boolean = false
)
