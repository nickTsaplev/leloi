package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.LikeRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import com.lesterade.services.models.UserDto
import com.lesterade.services.models.toMyDto

class UserServiceImpl(private val candidateRepository: CandidateRepository,
                      private val userRepository: UserRepository): UserService {

    override fun getUser(id: UserId): User {
        return userRepository.getUser(id)
    }

    override fun getCandidate(id: UserId): Candidate {
        return candidateRepository.getCandidate(userRepository.getUser(id).candidate)
    }

    override fun updateUserInfo(id: UserId, newVal: UserDto) {
        val user = candidateRepository.getCandidate(userRepository.getUser(id).candidate)

        user.contact = newVal.contact
        user.orientation = newVal.orientation
        user.description = newVal.description
        user.name = newVal.name
        user.gender = newVal.gender

        candidateRepository.updateCandidate(user)
    }

    override fun changePassword(id: UserId, newVal: ByteArray) {
        val user = userRepository.getUser(id)
        user.credential = newVal
        userRepository.updateUser(user)
    }

    override fun removeUser(id: UserId) {
        userRepository.removeUser(userRepository.getUser(id))
    }
}