package com.lesterade

import com.lesterade.controllers.configureSecurity
import com.lesterade.infrastructure.MemoryCandidateRepository
import com.lesterade.infrastructure.MemoryLikeRepository
import com.lesterade.infrastructure.MemoryUserRepository
import com.lesterade.infrastructure.PostgresCandidateRepository
import com.lesterade.infrastructure.PostgresUserRepository
import com.lesterade.infrastructure.PostresLikeRespository
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.LikeRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import com.lesterade.services.ConnectingService
import com.lesterade.services.ConnectingServiceImpl
import com.lesterade.services.UserService
import com.lesterade.services.UserServiceImpl
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

suspend fun Application.module() {
    install(CallLogging) {
        level = Level.INFO // Set the logging level (default is INFO)
        filter { call -> call.request.path().startsWith("/api") } // Log only specific paths
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
    install(ContentNegotiation) {
        json()
    }
    val database = exposedConnectPostgres()
    dependencies {
        provide <Database> { database }
        provide <CandidateRepository> { PostgresCandidateRepository() }
        provide <LikeRepository> { PostresLikeRespository() }
        provide <UserRepository> { PostgresUserRepository() }

        provide<ConnectingService> { ConnectingServiceImpl(resolve(), resolve(), resolve()) }
        provide<UserService> { UserServiceImpl(resolve(), resolve()) }
    }
    configureFrameworks()
    configureSerialization()
    // configureDatabases()
    configureRouting()
}
