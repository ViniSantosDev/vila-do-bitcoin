package com.bitcoingame.controller

import com.bitcoingame.service.GameService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class GameController(private val gameService: GameService) {


    @GetMapping("/")
    fun home(): String = "index"

    @PostMapping("/start")
    fun start(@RequestParam name: String): String {
        val player = gameService.createPlayer(name)
        return "redirect:/game/${player.id}"
    }

    // ── Tela principal do jogo ──────────────────────────────────────────────

    @GetMapping("/game/{id}")
    fun game(@PathVariable id: Long, model: Model): String {
        val player = gameService.getPlayer(id)
        model.addAttribute("player", player)
        model.addAttribute("breadPrice", 1 + player.kingMints)
        model.addAttribute("totalGold", 100 + player.kingMints * 50)
        return "game"
    }

    // ── Fase 1: Escambo ─────────────────────────────────────────────────────

    @PostMapping("/game/{id}/barter")
    fun barter(
        @PathVariable id: Long,
        @RequestParam item1: String,
        @RequestParam item2: String,
        model: Model
    ): String {
        val result = gameService.attemptBarter(id, item1, item2)
        val player = gameService.getPlayer(id)
        model.addAttribute("result", result)
        model.addAttribute("player", player)
        return "fragments/barter-result"
    }

    // ── Fase 2: Inflação ────────────────────────────────────────────────────

    @PostMapping("/game/{id}/mint")
    fun mint(@PathVariable id: Long, model: Model): String {
        val result = gameService.kingMints(id)
        val player = gameService.getPlayer(id)
        model.addAttribute("result", result)
        model.addAttribute("player", player)
        return "fragments/mint-result"
    }

    // ── Avançar fase ────────────────────────────────────────────────────────

    @PostMapping("/game/{id}/advance/{phase}")
    fun advance(
        @PathVariable id: Long,
        @PathVariable phase: Int,
        model: Model
    ): String {
        val player = gameService.advancePhase(id, phase)
        model.addAttribute("player", player)
        model.addAttribute("breadPrice", 1 + player.kingMints)
        model.addAttribute("totalGold", 100 + player.kingMints * 50)
        return when (phase) {
            2 -> "fragments/phase2 :: content"
            3 -> "fragments/phase3 :: content"
            4 -> "fragments/phase4 :: content"
            else -> "fragments/phase1 :: content"
        }
    }

    // ── Fase 3: Banco ───────────────────────────────────────────────────────

    @PostMapping("/game/{id}/bank")
    fun bank(@PathVariable id: Long, model: Model): String {
        val result = gameService.bankFails(id)
        val player = gameService.getPlayer(id)
        model.addAttribute("result", result)
        model.addAttribute("player", player)
        return "fragments/bank-result"
    }

    // ── Fase 4: Bitcoin ─────────────────────────────────────────────────────

    @PostMapping("/game/{id}/mine")
    fun mine(@PathVariable id: Long, model: Model): String {
        val result = gameService.mineBitcoin(id)
        val player = gameService.getPlayer(id)
        model.addAttribute("result", result)
        model.addAttribute("player", player)
        return "fragments/phase4 :: content"
    }

    @PostMapping("/game/{id}/send")
    fun send(@PathVariable id: Long, model: Model): String {
        val player = gameService.sendBitcoin(id)
        model.addAttribute("player", player)
        return "fragments/phase4 :: send-result"
    }

    // ── Reiniciar ───────────────────────────────────────────────────────────

    @PostMapping("/game/{id}/reset")
    fun reset(@PathVariable id: Long): String {
        gameService.resetGame(id)
        return "redirect:/game/$id"
    }
}
