package com.lesterade.domain

data class CandidateId(val value: Int) {
    fun toUserId() = UserId(value)
}
