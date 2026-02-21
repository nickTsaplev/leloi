package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId

interface CandidateRepository {
    fun getCandidate(id: CandidateId): Candidate
    fun getCandidates(query: CandidateQuery): Collection<Candidate>

    fun addCandidate(candidate: Candidate): Candidate
    fun updateCandidate(candidate: Candidate)
    fun removeCandidate(candidate: Candidate)
}