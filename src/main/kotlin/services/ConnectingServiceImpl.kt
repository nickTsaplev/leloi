package com.lesterade.services

import com.lesterade.domain.Candidate
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
        val collection = matcher.getConnections(user).toList()

        val numberSet = mutableSetOf<Int>()
        while (numberSet.size < min(collection.size / 2, 20)) {
            var next = Random.nextInt(collection.size)
            while (numberSet.contains(next))
                next = Random.nextInt(collection.size)
            numberSet.add(next)
        }

        return numberSet.map { collection[it] }
    }

    override fun getMyLikes(user: UserId): Collection<Candidate> {
        return likeRepository.getLikes(user).map { candidateRepository.getCandidate(it) }
    }

    override fun getContact(from: UserId, to: UserId): String {
        if (!likeRepository.doesLike(from, to))
            return ""

        if (!likeRepository.doesLike(to, from))
            return ""

        return candidateRepository.getCandidate(to).contact
    }

    override fun like(from: UserId, likes: UserId): Boolean {
        val user = userRepository.getUser(from)

        if (user.likeQuota == 0) {
            val current = java.time.Instant.now()
            if (current.toEpochMilli() - user.quotaReachedTimestamp > 1000 * 60 * 60 * 24) {
                user.likeQuota = 11
            } else
                return false
        }

        user.likeQuota--

        likeRepository.like(from, likes)

        if (user.likeQuota == 0) {
            val current = java.time.Instant.now()
            user.quotaReachedTimestamp = current.toEpochMilli()
        }

        userRepository.updateUser(user)
        return true
    }

    override fun skip(from: UserId, likes: UserId) { }
}