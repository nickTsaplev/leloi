package com.lesterade.domain

data class Candidate(
    var id: CandidateId,
    var gender: Gender,
    var orientation: Orientation,
    var name: String,
    var description: String,
    var contact: String
)

