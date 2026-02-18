package com.lesterade.services.models

import com.lesterade.domain.Candidate
import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val gender: Gender,
                   val orientation: Orientation,
                   val name: String,
                   val description: String,
                   val contact: String)

fun Candidate.toMyDto(): UserDto {
    return UserDto(gender, orientation, name, description, contact)
}