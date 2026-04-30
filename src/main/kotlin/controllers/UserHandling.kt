package com.lesterade.controllers

import com.lesterade.services.UserService
import com.lesterade.services.models.UserDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.configureUserHandling() {
    val userService: UserService by dependencies

    routing {
        post("/api/register/simple") {
            val name = call.parameters["login"]
            val password = call.parameters["password"]
            val secret = call.parameters["secret"]

            if (secret == "test" && name != null && password != null) {
                val id = userService.createUser(name, password).id
                call.respond(HttpStatusCode.Created, id.value)
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
        post("/api/register/complex") {
            val name = call.parameters["login"]
            val password = call.parameters["password"]
            val secret = call.parameters["secret"]
            val data = call.receive<UserDto>()

            if (secret == "test" && name != null && password != null) {
                val id = userService.createUserAndCandidate(name, password, data).id
                call.respond(HttpStatusCode.Created, id.value)
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }
}