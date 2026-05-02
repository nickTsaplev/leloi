package com.lesterade.infrastructure

import com.lesterade.domain.CandidateId
import com.lesterade.infrastructure.interfaces.LikeRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock

object LikeTable : IntIdTable("likes", "like_id") {
    val from = reference("from_user_id", CandidatesTable.id)
    val to = reference("to_user_id", CandidatesTable.id)

    val createdAt = timestamp("created_at")
}

class LikeEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<LikeEntity>(LikeTable);

    var from by LikeTable.from
    var to by LikeTable.to

    var createdAt by LikeTable.createdAt
}

class PostresLikeRespository: LikeRepository {
    override fun getLikes(to: CandidateId): Collection<CandidateId> {
        return transaction {
            LikeEntity.find { LikeTable.to eq to.value }.map { CandidateId(it.from.value) }
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
        transaction {
            LikeTable.insert {
                it[from] = fromId.value
                it[to] = toId.value
                it[createdAt] = Clock.System.now()
            }
        }
    }

    override fun unlike(fromId: CandidateId, toId: CandidateId) {
        transaction {
            val remove = UserEntity.find{ (LikeTable.from eq fromId.value) and (LikeTable.to eq toId.value) }
            remove.forEach {
                it.delete()
            }
        }
    }
}