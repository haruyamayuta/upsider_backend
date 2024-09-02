package jp.co.upsider.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

object Users : LongIdTable("Users") {
    val name = varchar("name", 255)
    val uuid = varchar("uuid", 255)
    val companyId = long("company_id")
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val createdAt = datetime("created_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
    val updatedAt = datetime("updated_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }

    fun fetchById(id: Long): User? {
        return transaction {
            User.find {
                Users.id eq(id)
            }.limit(1).firstOrNull()
        }
    }
}


interface IUser {
    var name: String
    var uuid: String
    var companyId: Long
    var email: String
    var password: String
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime
}


class User(id: EntityID<Long>): LongEntity(id), IUser {
    companion object : LongEntityClass<User>(Users)

    override var name by Users.name
    override var uuid by Users.uuid
    override var companyId by Users.companyId
    override var email by Users.email
    override var password by Users.password
    override var createdAt by Users.createdAt
    override var updatedAt by Users.updatedAt
}
