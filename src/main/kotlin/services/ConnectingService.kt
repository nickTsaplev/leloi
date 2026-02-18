package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.UserId

interface ConnectingService {
    fun getConnections(user: UserId): Collection<Candidate>
    fun getMyLikes(user: UserId): Collection<Candidate>

    fun getContact(from: UserId, to: UserId): String
    
    fun like(from: UserId, likes: UserId): Boolean
    fun skip(from: UserId, likes: UserId)
}