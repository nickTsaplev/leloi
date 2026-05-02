package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import com.lesterade.services.models.UserDto
import org.mindrot.jbcrypt.BCrypt

class UserServiceImpl(private val candidateRepository: CandidateRepository,
                      private val userRepository: UserRepository): UserService {

    override fun getUser(id: UserId): User {
        return userRepository.getUser(id)
    }

    override fun getUserByName(name: String): User {
        return userRepository.getUser(name)
    }

    override fun getCandidate(id: UserId): Candidate {
        return candidateRepository.getCandidate(userRepository.getUser(id).id.toCandidate())
    }

    override fun updateCandidateInfo(id: UserId, newVal: UserDto) {
        val user = candidateRepository.getCandidate(userRepository.getUser(id).id.toCandidate())

        user.contact = newVal.contact
        user.orientation = newVal.orientation
        user.description = newVal.description
        user.name = newVal.name
        user.gender = newVal.gender

        candidateRepository.updateCandidate(user)
    }

    override fun createUser(login: String, password: String, email: String): User {
        val user = userRepository.addUser(User(login, BCrypt.hashpw(password, BCrypt.gensalt()), email))
        candidateRepository.addCandidate(Candidate(user.id.toCandidate()))
        return user
    }

    override fun createUserAndCandidate(login: String, password: String, email: String, data: UserDto): User {
        val user = userRepository.addUser(
            User(login, BCrypt.hashpw(password, BCrypt.gensalt()), email))
        val candidate = candidateRepository.addCandidate(Candidate(
            user.id.toCandidate(),
            data.gender,
            data.orientation,
            data.name,
            data.description,
            data.contact
        ))
        return user
    }

    override fun changePassword(id: UserId, newVal: String) {
        val user = userRepository.getUser(id)
        user.credential = BCrypt.hashpw(newVal, BCrypt.gensalt())
        userRepository.updateUser(user)
    }

    override fun authorize(name: String, password: String): UserId? {
        val user = userRepository.getUser(name)
        if(!BCrypt.checkpw(password, user.credential))
            return null
        return user.id;
    }

    override fun removeUser(id: UserId) {
        userRepository.removeUser(userRepository.getUser(id))
    }
}