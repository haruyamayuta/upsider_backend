package jp.co.upsider.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

object Clients: LongIdTable("Clients"){
    val companyId = long("company_id")
    val corporateName = varchar("corporate_name", 255)
    val representativeName = varchar("representative_name", 255)
    val phoneNumber = varchar("phone_number", 20)
    val postalCode = varchar("postal_code", 10)
    val address = text("address")
    val createdAt = datetime("created_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
    val updatedAt = datetime("updated_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }

    fun fetchById(id:Long):Client?{
        return transaction {
            Client.find{
                Clients.id eq(id)
            }.limit(1).firstOrNull()
        }
    }

    fun fetchByIds(id: List<Long>): List<Client> {
        return transaction {
            Client.find { Clients.id inList id }.toList()
        }
    }
}

interface IClient {
    var companyId: Long
    var corporateName: String
    var representativeName: String
    var phoneNumber: String
    var postalCode: String
    var address: String
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime
}

class Client(id: EntityID<Long>): LongEntity(id), IClient{
    companion object : LongEntityClass<Client>(Clients)

    override var companyId by Clients.companyId
    override var corporateName by Clients.corporateName
    override var representativeName by Clients.representativeName
    override var phoneNumber by Clients.phoneNumber
    override var postalCode by Clients.postalCode
    override var address by Clients.address
    override var createdAt by Clients.createdAt
    override var updatedAt by Clients.updatedAt
}
