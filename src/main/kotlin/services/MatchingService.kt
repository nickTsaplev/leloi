package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.UserId

interface MatchingService {
    fun getConnections(user: UserId): Collection<Candidate>
}