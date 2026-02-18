package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.UserId

interface LikeRepository {
    fun getLikes(to: UserId): Collection<UserId>
    fun doesLike(from: UserId, to: UserId): Boolean

    fun like(from: UserId, to: UserId)
    fun unlike(from: UserId, to: UserId)
}