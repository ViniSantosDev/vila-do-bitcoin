package com.bitcoingame.service

import com.bitcoingame.exception.PlayerNotFoundException
import com.bitcoingame.model.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GameService(private val playerRepository: PlayerRepository) {

    fun createPlayer(name: String): Player {
        val player = Player(name = name.trim().ifBlank { "Bitcoiner" })
        return playerRepository.save(player)
    }

    fun getPlayer(id: Long): Player =
        playerRepository.findById(id).orElseThrow { PlayerNotFoundException(id) }

    fun attemptBarter(playerId: Long, item1: String, item2: String): TradeResult {
        val player = getPlayer(playerId)
        player.barterAttempts++
        playerRepository.save(player)

        val messages = mapOf(
            "apple-bread" to "Você ofereceu 🍎 maçã pela 🍞. A Ana queria leite, não maçã! Recusado.",
            "apple-milk"  to "Maçã por leite? O Pedro só aceita pão. A troca falhou!",
            "apple-fish"  to "O pescador queria pão. Sem troca direta possível!",
            "bread-milk"  to "Pão por leite? Pedro quer maçã. Ninguém consegue o que quer!",
            "bread-fish"  to "Pão por peixe? O pescador queria leite. Falhou!",
            "milk-fish"   to "Leite por peixe? A troca não aconteceu. Precisamos de algo melhor..."
        )

        val key = listOf(item1, item2).sorted().joinToString("-")
        val message = messages[key] ?: "Essa troca não funcionou!"
        val showNext = player.barterAttempts >= 2

        return TradeResult(
            message = message,
            isSuccess = false,
            showNextButton = showNext
        )
    }

    fun advancePhase(playerId: Long, toPhase: Int): Player {
        val player = getPlayer(playerId)
        if (toPhase == player.currentPhase + 1) {
            player.currentPhase = toPhase
            if (toPhase == 3) {
                player.goldCoins = 10 // o banco quebrou, o jogador perdeu tudo
            }
            playerRepository.save(player)
        }
        return player
    }

    fun kingMints(playerId: Long): MintResult {
        val player = getPlayer(playerId)
        player.kingMints++
        playerRepository.save(player)

        val totalGold = 100 + player.kingMints * 50
        val breadPrice = 1 + player.kingMints
        val message = "👑 O Rei criou mais ${50} moedas! Agora o pão custa $breadPrice moedas. Seu ouro vale menos!"
        val showNext = player.kingMints >= 2

        return MintResult(
            totalGold = totalGold,
            breadPrice = breadPrice,
            message = message,
            showNextButton = showNext
        )
    }

    fun mineBitcoin(playerId: Long): MiningResult {
        val player = getPlayer(playerId)

        player.mineCount++
        player.bitcoins++
        player.miningComplete = true
        playerRepository.save(player)

        return MiningResult(
            progress = 100,
            complete = true,
            bitcoins = player.bitcoins
        )
    }

    fun sendBitcoin(playerId: Long): Player {
        val player = getPlayer(playerId)
        if (player.bitcoins > 0) {
            player.bitcoins--
            player.gameComplete = true
            playerRepository.save(player)
        }
        return player
    }

    fun resetGame(playerId: Long): Player {
        val player = getPlayer(playerId)
        val reset = player.copy(
            currentPhase = 1,
            barterAttempts = 0,
            goldCoins = 10,
            kingMints = 0,
            bitcoins = 0,
            mineCount = 0,
            miningComplete = false,
            gameComplete = false
        )
        return playerRepository.save(reset)
    }

    fun bankFails(playerId: Long): BankResult {
        val player = getPlayer(playerId)
        val goldLost = player.goldCoins
        player.goldCoins = 0
        playerRepository.save(player)

        val message = "😭 O banco faliu e fechou as portas. Você perdeu tudo o que tinha guardado!"

        return BankResult(
            message = message,
            goldLost = goldLost,
            showNextButton = true
        )
    }
}
