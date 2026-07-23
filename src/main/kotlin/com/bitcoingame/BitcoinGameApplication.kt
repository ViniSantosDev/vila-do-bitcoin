package com.bitcoingame

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BitcoinGameApplication

fun main(args: Array<String>) {
    runApplication<BitcoinGameApplication>(*args)
}
