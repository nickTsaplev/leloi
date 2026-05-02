package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateRepository
import com.lesterade.infrastructure.interfaces.LikeRepository
import com.lesterade.infrastructure.interfaces.UserRepository
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Clock

class ConnectingServiceImpl(private val candidateRepository: CandidateRepository,
                            private val likeRepository: LikeRepository,
                            private val userRepository: UserRepository): ConnectingService {
    private val matcher = DumbMatcher(candidateRepository)

    override fun getConnections(user: UserId): Collection<Candidate> {
        return matcher.getConnections(userRepository.getUser(user).id.toCandidate()).toList()
    }

    override fun getMyLikes(user: UserId): Collection<Candidate> {
        return likeRepository.getLikes(userRepository.getUser(user).id.toCandidate()).map { candidateRepository.getCandidate(it) }
    }

    override fun getContact(from: UserId, to: CandidateId): String {
        val fromC = userRepository.getUser(from).id.toCandidate()
        if (!likeRepository.doesLike(fromC, to))
            return ""

        if (!likeRepository.doesLike(to, fromC))
            return ""

        return candidateRepository.getCandidate(to).contact
    }

    override fun like(from: UserId, likes: CandidateId): Boolean {
        val user = userRepository.getUser(from)

        /*if (user.likeQuota == 0) {
            if ((Clock.System.now() - user.quotaReachedTimestamp).inWholeHours > 24) {
                user.likeQuota = 11
            } else
                return false
        }

        user.likeQuota--

        likeRepository.like(user.id.toCandidate(), likes)

        if (user.likeQuota == 0) {
            user.quotaReachedTimestamp = Clock.System.now()
        }*/

        likeRepository.like(user.id.toCandidate(), likes)

        //userRepository.updateUser(user)
        return true
    }

    override fun skip(from: UserId, likes: CandidateId) { }
}