package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId

interface MatchingService {
    fun getConnections(candidate: CandidateId): Collection<Candidate>
}