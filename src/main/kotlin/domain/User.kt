package com.lesterade.domain

class User(
    var id: UserId,

    val candidate: CandidateId,
    var credential: ByteArray,

    var likeQuota: Int,
    var quotaReachedTimestamp: Long
)
