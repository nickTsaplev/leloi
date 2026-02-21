package com.lesterade.infrastructure

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateQuery
import com.lesterade.infrastructure.interfaces.CandidateRepository

class MemoryCandidateRepository: CandidateRepository {
    private val canList = mutableListOf<Candidate>()

    override fun getCandidate(id: CandidateId): Candidate {
        return canList.first { it.id == id }
    }

    override fun getCandidates(query: CandidateQuery): Collection<Candidate> {
        return canList.filter { query.gender.isEmpty() || it.gender in query.gender }
            .filter { query.orientation.isEmpty() || it.orientation in query.orientation}.toList().shuffled().take(query.amount)
    }

    override fun addCandidate(candidate: Candidate): Candidate {
        candidate.id = CandidateId(canList.size - 1)
        canList.add(candidate)
        return candidate
    }

    override fun updateCandidate(candidate: Candidate) {
        canList[canList.indexOfFirst { it.id == candidate.id }] = candidate
    }

    override fun removeCandidate(candidate: Candidate) {
        canList.removeAt(canList.indexOfFirst { it.id == candidate.id })
    }
}