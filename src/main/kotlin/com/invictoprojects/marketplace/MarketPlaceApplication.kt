package com.invictoprojects.marketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MarketPlaceApplication

fun main(args: Array<String>) {
    runApplication<MarketPlaceApplication>(*args)
}
