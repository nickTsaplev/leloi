package com.lesterade

import com.lesterade.infrastructure.MemoryCandidateRepository
import com.lesterade.infrastructure.MemoryLikeRepository
import com.lesterade.infrastructure.MemoryUserRepository
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.LikeRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import com.lesterade.services.ConnectingService
import com.lesterade.services.ConnectingServiceImpl
import com.lesterade.services.UserService
import com.lesterade.services.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    dependencies {
        provide <CandidateRepository> { MemoryCandidateRepository() }
        provide <LikeRepository> { MemoryLikeRepository() }
        provide <UserRepository> { MemoryUserRepository() }

        provide<ConnectingService> { ConnectingServiceImpl(resolve(), resolve(), resolve()) }
        provide<UserService> { UserServiceImpl(resolve(), resolve()) }
    }
    configureFrameworks()
    configureHTTP()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
