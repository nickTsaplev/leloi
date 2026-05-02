package com.lesterade.infrastructure

import com.lesterade.domain.Candidate
import com.lesterade.domain.CandidateId
import com.lesterade.domain.Gender
import com.lesterade.domain.Orientation
import com.lesterade.domain.User
import com.lesterade.domain.UserId
import com.lesterade.infrastructure.interfaces.UserRepository
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object UsersTable : IntIdTable("users", "user_id") {
    val login = varchar("username", 50)
    val password = varchar("password", 1024)

    val email = varchar("email", 128)
    var createdAt = timestamp("created_at")
}

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password

    var email by UsersTable.email

    var createdAt by UsersTable.createdAt
}

fun UserEntity.toUser(): User {
    return User(UserId(id.value), login, password, email, createdAt)
}

class PostgresUserRepository: UserRepository {
    override fun getUser(id: UserId): User {
        return transaction {
            UserEntity[id.value].toUser()
        }
    }

    override fun getUser(name: String): User {
        return transaction {
            UserEntity.find { UsersTable.login eq name }.first().toUser()
        }
    }

    override fun addUser(user: User): User {
        transaction {
            val added = UserEntity.new {
                login = user.login
                password = user.credential
                email = user.email
                createdAt = user.createdAt
            }
            user.id = UserId(added.id.value)
        }
        return user
    }

    override fun removeUser(user: User) {
        transaction {
            UserEntity[user.id.value].delete()
        }
    }

    override fun updateUser(user: User) {
        transaction {
            val changed = UserEntity[user.id.value]
            changed.login = user.login
            changed.password = user.credential
            changed.email = user.email
            changed.createdAt = user.createdAt
        }
    }
}