package com.lesterade.controllers

import com.lesterade.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set

fun Application.configureSecurity() {
    val userService: UserService by dependencies

    install(Authentication) {
        basic(name = "auth-basic") {
            realm = "Ktor Server"
            validate { credentials ->
                userService.authorize(credentials.name, credentials.password)?.let {
                    UserIdPrincipal(credentials.name)
                }
            }
        }
    }

    routing() {
        authenticate("auth-basic") {
            post("/login") {
                val principal = call.authentication.principal<UserIdPrincipal>()!!
                val user = userService.getUserByName(principal.name)
                call.sessions.set(UserSession(id = user.id.value))
            }

            post("/logout") {
                call.sessions.clear<UserSession>()
            }
        }
    }
}
