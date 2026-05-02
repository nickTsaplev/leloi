package com.lesterade.infrastructure.interfaces

import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import com.lesterade.domain.UserId

data class CandidateQuery(
    val gender: Collection<Gender> = listOf(),
    val orientation: Collection<Orientation> = listOf(),
    val bans: Collection<UserId> = listOf(),
    val amount: Int = 20
)