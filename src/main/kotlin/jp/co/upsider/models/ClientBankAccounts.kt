package jp.co.upsider.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime


object ClientBankAccounts: LongIdTable("client_bank_accounts") {
    val clientId = integer("client_id")
    val bankName = varchar("bank_name", 255)
    val branchName = varchar("branch_name", 255)
    val accountNumber = varchar("account_type", 20)
    val accountName = varchar("account_number", 255)
    val createdAt = datetime("created_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
    val updatedAt = datetime("updated_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
}

interface IClientBankAccount {
    var clientId: Int
    var bankName: String
    var branchName: String
    var accountNumber: String
    var accountName: String
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime
}

class ClientBankAccount(id: EntityID<Long>): LongEntity(id), IClientBankAccount {
    companion object : LongEntityClass<ClientBankAccount>(ClientBankAccounts)

    override var clientId by ClientBankAccounts.clientId
    override var bankName by ClientBankAccounts.bankName
    override var branchName by ClientBankAccounts.branchName
    override var accountNumber by ClientBankAccounts.accountNumber
    override var accountName by ClientBankAccounts.accountName
    override var createdAt by ClientBankAccounts.createdAt
    override var updatedAt by ClientBankAccounts.updatedAt
}

