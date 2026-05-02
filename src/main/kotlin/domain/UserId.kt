package com.lesterade.domain

@JvmInline
value class UserId(val value: Int) {
    fun toCandidate(): CandidateId = CandidateId(value)
}
