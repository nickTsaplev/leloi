package com.lesterade.controllers

import com.lesterade.domain.UserId
import com.lesterade.services.UserService
import com.lesterade.services.models.toDto
import com.lesterade.services.models.toMyDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.configureCandidates() {
    val userService: UserService by dependencies

    routing() {
        authenticate("auth-basic") {
            put("/api/candidates/myself") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    userService.updateCandidateInfo(session, call.receive())
                    call.respond(HttpStatusCode.OK)
                } else
                    call.respond(HttpStatusCode.Unauthorized)
            }
            delete("/api/candidates/myself") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    userService.removeUser(session)
                    call.respond(HttpStatusCode.OK)
                } else
                    call.respond(HttpStatusCode.Unauthorized)
            }
            get("/api/candidates/myself") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    call.respond(HttpStatusCode.OK, userService.getCandidate(session).toMyDto())
                } else
                    call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}