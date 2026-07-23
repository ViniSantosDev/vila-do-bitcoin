package com.bitcoingame.model

data class TradeResult(
    val message: String?,
    val isSuccess: Boolean,
    val showNextButton: Boolean
)

data class MintResult(
    val totalGold: Int,
    val breadPrice: Int,
    val message: String,
    val showNextButton: Boolean
)

data class MiningResult(
    val progress: Int,
    val complete: Boolean,
    val bitcoins: Int
)
