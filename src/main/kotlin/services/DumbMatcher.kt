package com.lesterade.services

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateQuery
import com.lesterade.infrastructure.interfaces.CandidateRepository

class DumbMatcher(private val candidateRepository: CandidateRepository): MatchingService {
    override fun getConnections(candidate: CandidateId): Collection<Candidate> {
        val userObj = candidateRepository.getCandidate(candidate)

        val query = CandidateQuery(
            when (userObj.orientation) {
                Orientation.Men -> listOf(Gender.Male)
                Orientation.Women -> listOf(Gender.Female)
                else
                    -> listOf()
            }, when(userObj.gender) {
                Gender.Male -> listOf(Orientation.Men, Orientation.Anyone)
                Gender.Female -> listOf(Orientation.Women, Orientation.Anyone)
                Gender.Other -> listOf(Orientation.Anyone)
            }, listOf(userObj.id.toUserId()), 20)

        return candidateRepository.getCandidates(query)
    }
}