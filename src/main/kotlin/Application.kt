package com.lesterade

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureHTTP()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
