package com.lesterade.controllers

import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId
import com.lesterade.services.ConnectingService
import com.lesterade.services.models.toDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.di.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureConnections() {
    val connector: ConnectingService by dependencies
    routing {
        authenticate("auth-basic") {

            get("/api/candidates") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    call.respond(HttpStatusCode.OK, connector.getConnections(session).map { it.toDto() })
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }

            get("/api/likes") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    call.respond(HttpStatusCode.OK, connector.getMyLikes(session).map { it.toDto() })
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }


            post("/api/like") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    val liked = call.parameters["liked"]
                    if (liked != null) {
                        val id = CandidateId(liked.toInt())
                        connector.like(session, id)
                        call.respond(HttpStatusCode.OK)
                    } else
                        call.respond(HttpStatusCode.BadRequest)
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }



            post("/api/respond") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    val liked = call.parameters["liked"]
                    if (liked != null) {
                        val id = CandidateId(liked.toInt())
                        connector.like(session, id)
                        call.respond(HttpStatusCode.OK, connector.getContact(session, id))
                    } else
                        call.respond(HttpStatusCode.BadRequest)
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }



            post("/api/skip") {
                val session = call.principal<UserIdPrincipal>()?.name?.toInt()?.let { UserId(it) }
                if (session != null) {
                    val liked = call.parameters["liked"]
                    if (liked != null) {
                        val id = CandidateId(liked.toInt())
                        connector.skip(session, id)
                        call.respond(HttpStatusCode.OK)
                    } else
                        call.respond(HttpStatusCode.BadRequest)
                } else
                    call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}