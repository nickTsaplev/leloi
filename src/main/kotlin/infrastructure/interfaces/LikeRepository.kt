package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.CandidateId

interface LikeRepository {
    fun getLikes(to: CandidateId): Collection<CandidateId>
    fun doesLike(from: CandidateId, to: CandidateId): Boolean

    fun like(fromId: CandidateId, toId: CandidateId)
    fun unlike(fromId: CandidateId, toId: CandidateId)
}