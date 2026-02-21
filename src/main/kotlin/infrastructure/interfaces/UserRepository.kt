package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.User
import com.lesterade.domain.UserId

interface UserRepository {
    fun getUser(id: UserId): User

    fun addUser(user: User): User
    fun removeUser(user: User)
    fun updateUser(user: User)
}