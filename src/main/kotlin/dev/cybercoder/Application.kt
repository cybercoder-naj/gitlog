package dev.cybercoder

import dev.cybercoder.plugins.configureMonitoring
import dev.cybercoder.plugins.configureSerialization
import dev.cybercoder.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(args)
    }.start(wait = true)
}

fun Application.module(args: Array<String>) {
    configureMonitoring()
    configureSerialization()

    configureRouting(args)
}
