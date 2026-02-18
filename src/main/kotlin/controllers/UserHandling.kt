package com.lesterade.controllers

import com.lesterade.domain.UserId
import com.lesterade.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import kotlin.text.toInt

fun Application.ConfigureUserHandling() {
    val userman: UserService by dependencies

    routing {
        post("/login") {

        }
    }
}