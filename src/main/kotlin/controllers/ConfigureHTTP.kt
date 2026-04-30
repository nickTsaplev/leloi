package com.lesterade.controllers

import io.ktor.server.application.Application

fun Application.configureHTTP() {
    configureSecurity()
    configureUserHandling()
    configureConnections()
    configureCandidates()
}