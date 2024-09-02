
package jp.co.upsider.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

object Companies : LongIdTable("companies") {
    val name = varchar("name", 255)
    val address = text("address")
    val phoneNumber = varchar("phone_number", 20)
    val postalCode = varchar("postal_code", 10)
    val createdAt = datetime("created_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
    val updatedAt = datetime("updated_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
}

interface ICompany {
    var name: String
    var address: String
    var phoneNumber: String
    var postalCode: String
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime
}

class Company(id: EntityID<Long>): LongEntity(id), ICompany{
    companion object : LongEntityClass<Company>(Companies)

    override var name by Companies.name
    override var address by Companies.address
    override var phoneNumber by Companies.phoneNumber
    override var postalCode by Companies.postalCode
    override var createdAt by Companies.createdAt
    override var updatedAt by Companies.updatedAt
}