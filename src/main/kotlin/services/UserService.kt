package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.services.models.UserDto

interface UserService {
    fun getUser(id: UserId): User
    fun getCandidate(id: UserId): Candidate

    fun updateUserInfo(id: UserId, newVal: UserDto)

    fun changePassword(id: UserId, newVal: ByteArray)

    fun removeUser(id: UserId)
}