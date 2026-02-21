package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId

interface ConnectingService {
    fun getConnections(user: UserId): Collection<Candidate>
    fun getMyLikes(user: UserId): Collection<Candidate>

    fun getContact(from: UserId, to: CandidateId): String
    
    fun like(from: UserId, likes: CandidateId): Boolean
    fun skip(from: UserId, likes: CandidateId)
}