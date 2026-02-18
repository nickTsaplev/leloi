package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.Candidate
import com.lesterade.domain.UserId

interface CandidateRepository {
    fun getCandidate(id: UserId): Candidate
    fun getCandidates(query: CandidateQuery): Collection<Candidate>

    fun addCandidate(candidate: Candidate)
    fun updateCandidate(candidate: Candidate)
    fun removeCandidate(candidate: Candidate)
}