package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.LikeRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import kotlin.math.min
import kotlin.random.Random

class ConnectingServiceImpl(private val candidateRepository: CandidateRepository,
                            private val likeRepository: LikeRepository,
                            private val userRepository: UserRepository): ConnectingService {
    private val matcher = DumbMatcher(candidateRepository)

    override fun getConnections(user: UserId): Collection<Candidate> {
        return matcher.getConnections(userRepository.getUser(user).candidate).toList()
    }

    override fun getMyLikes(user: UserId): Collection<Candidate> {
        return likeRepository.getLikes(userRepository.getUser(user).candidate).map { candidateRepository.getCandidate(it) }
    }

    override fun getContact(from: UserId, to: CandidateId): String {
        val fromC = userRepository.getUser(from).candidate
        if (!likeRepository.doesLike(fromC, to))
            return ""

        if (!likeRepository.doesLike(to, fromC))
            return ""

        return candidateRepository.getCandidate(to).contact
    }

    override fun like(from: UserId, likes: CandidateId): Boolean {
        val user = userRepository.getUser(from)

        if (user.likeQuota == 0) {
            val current = java.time.Instant.now()
            if (current.toEpochMilli() - user.quotaReachedTimestamp > 1000 * 60 * 60 * 24) {
                user.likeQuota = 11
            } else
                return false
        }

        user.likeQuota--

        likeRepository.like(user.candidate, likes)

        if (user.likeQuota == 0) {
            val current = java.time.Instant.now()
            user.quotaReachedTimestamp = current.toEpochMilli()
        }

        userRepository.updateUser(user)
        return true
    }

    override fun skip(from: UserId, likes: CandidateId) { }
}