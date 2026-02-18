package com.lesterade.controllers

import com.lesterade.domain.UserId
import com.lesterade.services.ConnectingService
import com.lesterade.services.models.toDto
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.di.*
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import java.sql.Connection
import java.sql.DriverManager

fun Application.ConfigueConnections() {
    val connector: ConnectingService by dependencies

    routing {
        get("/candidates") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                call.respond(HttpStatusCode.OK, connector.getConnections(session.id).map{it.toDto()})
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }

    routing {
        get("/likes") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                call.respond(HttpStatusCode.OK, connector.getMyLikes(session.id).map{it.toDto()})
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }

    routing {
        post("/like") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                val userId = call.parameters["user"]
                if (userId != null) {
                    val id = UserId(userId.toInt())
                    connector.like(session.id, id)
                    call.respond(HttpStatusCode.OK)
                } else
                    call.respond(HttpStatusCode.BadRequest)
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }

    routing {
        post("/respond") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                val userId = call.parameters["user"]
                if (userId != null) {
                    val id = UserId(userId.toInt())
                    connector.like(session.id, id)
                    call.respond(HttpStatusCode.OK, connector.getContact(session.id, id))
                } else
                    call.respond(HttpStatusCode.BadRequest)
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }

    routing {
        post("/skip") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                val userId = call.parameters["user"]
                if (userId != null) {
                    val id = UserId(userId.toInt())
                    connector.skip(session.id, id)
                    call.respond(HttpStatusCode.OK)
                } else
                    call.respond(HttpStatusCode.BadRequest)
            } else
                call.respond(HttpStatusCode.BadRequest)
        }
    }
}