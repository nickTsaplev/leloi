package com.lesterade.infrastructure

import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.UserRepository

class MemoryUserRepository: UserRepository {
    val users = mutableListOf<User>()

    override fun getUser(id: UserId): User {
        return users.first {it.id == id}
    }

    override fun addUser(user: User) {
        users.add(user)
    }

    override fun removeUser(user: User) {
        users.remove(user)
    }

    override fun updateUser(user: User) {
        users[users.indexOfFirst { it.id == user.id }] = user
    }
}