package com.lesterade.services.models

import com.lesterade.domain.Candidate
import com.lesterade.domain.Gender
import kotlinx.serialization.Serializable

@Serializable
data class CandidateDto(val id: Int,
                        val gender: Gender,
                        val name: String,
                        val description: String)

fun Candidate.toDto(): CandidateDto {
    return CandidateDto(id.value, gender, name, description)
}