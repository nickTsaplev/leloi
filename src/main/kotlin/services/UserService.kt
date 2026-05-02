package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.services.models.UserDto

interface UserService {
    fun getUser(id: UserId): User
    fun getUserByName(name: String): User
    fun getCandidate(id: UserId): Candidate

    fun createUser(login: String, password: String, email: String): User
    fun createUserAndCandidate(login: String, password: String, email: String, data: UserDto): User

    fun updateCandidateInfo(id: UserId, newVal: UserDto)

    fun changePassword(id: UserId, newVal: String)

    fun removeUser(id: UserId)

    fun authorize(name: String, password: String): UserId?
}