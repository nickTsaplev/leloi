package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation

data class CandidateQuery(
    val gender: Collection<Gender> = listOf(),
    val orientation: Collection<Orientation> = listOf(),
    val amount: Int = 20
)