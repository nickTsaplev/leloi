package com.lesterade.controllers

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.header

fun Application.configureHTTP() {
    install(Sessions) {
        cookie<UserSession>("user_session", SessionStorageMemory())
    }

    configureSecurity()
    configureUserHandling()
    configureConnections()
    configureCandidates()
}