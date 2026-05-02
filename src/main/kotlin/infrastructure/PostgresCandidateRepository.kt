package com.lesterade.infrastructure

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.CandidateQuery
import com.lesterade.infrastructure.interfaces.CandidateRepository
import org.jetbrains.exposed.v1.core.Random
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.datetime.*
import org.postgresql.util.PGobject

class PGEnum<T:Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}


object CandidatesTable : IdTable<Int>("candidates") {
    override val id = reference("user_id", UsersTable.id)
    val gender = customEnumeration(
        "gender",
        "Gender",  // PostgreSQL enum type name
        { value -> when(value) {
            is PGobject -> Gender.valueOf((value as PGobject).value ?: "Other")
            is String -> Gender.valueOf(value)
            else -> error("Unknown gender")} },
        { PGEnum("Public.Gender", it) }
    )
        //enumeration<Gender>("gender")
    val orientation = customEnumeration(
            "orientation",
            "Orientation",  // PostgreSQL enum type name
            { value -> when(value) {
                is PGobject -> Orientation.valueOf((value as PGobject).value ?: "Anyone")
                is String -> Orientation.valueOf(value)
                else -> error("Unknown orientation")} },
            { PGEnum("Public.Orientation", it) }
        )
    //    enumeration<Orientation>("orientation")
    val name = varchar("name", 128)

    val description = text("description")
    val contact = varchar("contact", 128)
}

class CandidateDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CandidateDao>(CandidatesTable)

    var name by CandidatesTable.name
    var gender by CandidatesTable.gender
    var orientation by CandidatesTable.orientation
    var description by CandidatesTable.description
    var contact by CandidatesTable.contact
}

fun CandidateDao.toCandidate(): Candidate {
    return Candidate(CandidateId(id.value), gender, orientation, name, description, contact)
}

class PostgresCandidateRepository: CandidateRepository {
    override fun getCandidate(id: CandidateId): Candidate {
        return transaction {
            CandidateDao.find{ CandidatesTable.id eq id.value }.first()
        }.toCandidate()
    }

    override fun getCandidates(query: CandidateQuery): Collection<Candidate> {
        return transaction {
            val ans = CandidatesTable.selectAll()
            if(query.gender.isNotEmpty())
                ans.andWhere { CandidatesTable.gender inList query.gender }
            if(query.orientation.isNotEmpty())
                ans.andWhere { CandidatesTable.orientation inList query.orientation }
            if(query.bans.isNotEmpty())
                ans.andWhere { CandidatesTable.id notInList query.bans.map{ it.value } }
            ans.orderBy(Random())
            ans.limit(query.amount)

            CandidateDao.wrapRows(ans).map { it.toCandidate() }
        }
    }

    override fun addCandidate(candidate: Candidate): Candidate {
        transaction {
            val added = CandidateDao.new(candidate.id.value) {
                gender = candidate.gender
                orientation = candidate.orientation
                name = candidate.name
                description = candidate.description
                contact = candidate.contact
            }
        }
        return candidate
    }

    override fun updateCandidate(candidate: Candidate) {
        transaction {
            val changed = CandidateDao.find{ CandidatesTable.id eq candidate.id.value }.first()
            changed.gender = candidate.gender
            changed.orientation = candidate.orientation
            changed.name = candidate.name
            changed.description = candidate.description
            changed.contact = candidate.contact
        }
    }

    override fun removeCandidate(candidate: Candidate) {
        transaction {
            val remove = CandidateDao.find{ CandidatesTable.id eq candidate.id.value }.first()
            remove.delete()
        }
    }
}