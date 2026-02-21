package com.lesterade.infrastructure

import com.lesterade.domain.CandidateId
import com.lesterade.infrastructure.interfaces.LikeRepository

class MemoryLikeRepository: LikeRepository {
    private val likes = mutableListOf<Pair<CandidateId, CandidateId>>()

    override fun getLikes(to: CandidateId): Collection<CandidateId> {
        return likes.filter { it.second == to }.map { it.first }
    }

    override fun doesLike(from: CandidateId, to: CandidateId): Boolean {
        return likes.contains(from to to)
    }

    override fun like(fromId: CandidateId, toId: CandidateId) {
        likes.add(fromId to toId)
    }

    override fun unlike(fromId: CandidateId, toId: CandidateId) {
        likes.remove(fromId to toId)
    }
}