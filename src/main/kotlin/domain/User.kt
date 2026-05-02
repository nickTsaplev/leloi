package com.lesterade.domain

import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class User(
    var id: UserId,
    var login: String,

    var credential: String,

    val email: String,
    val createdAt: Instant,
) {

    constructor(login: String, password: String, email: String) :
            this(UserId(0), login, password, email, Clock.System.now()) {
    }
}
