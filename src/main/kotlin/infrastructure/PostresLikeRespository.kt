package com.lesterade.infrastructure

import com.lesterade.domain.CandidateId
import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import com.lesterade.infrastructure.interfaces.LikeRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object LikeTable : IntIdTable("likes") {
    val from = reference("from", CandidatesTable.id)
    val to = reference("to", CandidatesTable.id)
}

class LikeDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<LikeDao>(LikeTable);

    var from by LikeTable.from
    var to by LikeTable.to
}

class PostresLikeRespository: LikeRepository {
    override fun getLikes(to: CandidateId): Collection<CandidateId> {
        return transaction {
            LikeDao.find { LikeTable.to eq to.value }.map { CandidateId(it.from.value) }
        }
    }

    override fun doesLike(
        from: CandidateId,
        to: CandidateId
    ): Boolean {
        return !(transaction {
            LikeTable.selectAll().where{ (LikeTable.to eq to.value) and (LikeTable.to eq to.value) }.empty()
        })
    }

    override fun like(fromId: CandidateId, toId: CandidateId) {
        TODO("Not yet implemented")
    }

    override fun unlike(fromId: CandidateId, toId: CandidateId) {
        TODO("Not yet implemented")
    }
}