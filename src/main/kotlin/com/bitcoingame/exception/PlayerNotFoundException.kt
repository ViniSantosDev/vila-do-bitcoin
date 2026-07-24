package com.bitcoingame.exception

class PlayerNotFoundException(playerId: Long) :
    RuntimeException("Jogador com id $playerId não encontrado")