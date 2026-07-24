package com.bitcoingame.exception

import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(PlayerNotFoundException::class)
    fun handlePlayerNotFound(ex: PlayerNotFoundException, model: Model): String {
        logger.warn("Jogador não encontrado: {}", ex.message)
        model.addAttribute("title", "😅 Ops!")
        model.addAttribute("message", "Não encontramos seu jogo. Isso pode acontecer se o servidor reiniciou. Que tal começar de novo?")
        return "error-generic"
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, model: Model): String {
        logger.error("Erro inesperado na aplicação", ex)
        model.addAttribute("title", "😵 Algo deu errado")
        model.addAttribute("message", "Encontramos um problema inesperado. Por favor, tente novamente.")
        return "error-generic"
    }
}