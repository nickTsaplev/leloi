package com.lesterade.infrastructure

import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.LikeRepository

class MemoryLikeRepository: LikeRepository {
    private val likes = mutableListOf<Pair<UserId, UserId>>()

    override fun getLikes(to: UserId): Collection<UserId> {
        return likes.filter { it.second == to }.map { it.first }
    }

    override fun doesLike(from: UserId, to: UserId): Boolean {
        return likes.contains(from to to)
    }

    override fun like(from: UserId, to: UserId) {
        likes.add(from to to)
    }

    override fun unlike(from: UserId, to: UserId) {
        likes.remove(from to to)
    }
}