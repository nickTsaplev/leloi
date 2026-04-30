package com.lesterade

import com.lesterade.controllers.configureConnections
import com.lesterade.controllers.configureHTTP
import com.lesterade.controllers.configureUserHandling
import io.ktor.http.*
import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.openapi.OpenApiDocSource

fun Application.configureRouting() {
    configureHTTP()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    routing {
        swaggerUI(path = "/swagger") {
            info = OpenApiInfo("My API", "1.0")
            source = OpenApiDocSource.Routing(ContentType.Application.Json) {
                routingRoot.descendants()
            }
        }
    }
}
