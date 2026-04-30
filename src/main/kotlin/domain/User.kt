package com.lesterade.domain

class User(
    var id: UserId,
    var login: String,

    val candidate: CandidateId,
    var credential: String,

    var likeQuota: Int,
    var quotaReachedTimestamp: Long) {

    constructor(login: String, password: String, candidate: CandidateId) :
            this(UserId(0), login, candidate, password, 11, 0) {
    }
}
