package com.bitcoingame.exception

import java.util.UUID

class PlayerNotFoundException(playerId: UUID) :
    RuntimeException("Jogador com id $playerId não encontrado")