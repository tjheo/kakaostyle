package com.example.kakaostyle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class KakaostyleApplication

fun main(args: Array<String>) {
    runApplication<KakaostyleApplication>(*args)
}
