package com.lesterade.domain

class User(
    var id: UserId,
    var credential: ByteArray,

    var likeQuota: Int,
    var quotaReachedTimestamp: Long
)
