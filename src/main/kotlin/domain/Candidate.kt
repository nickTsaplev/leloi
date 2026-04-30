package com.lesterade.domain

data class Candidate(
    var id: CandidateId = CandidateId(0),
    var gender: Gender = Gender.Other,
    var orientation: Orientation = Orientation.Anyone,
    var name: String = "",
    var description: String = "",
    var contact: String = ""
)

