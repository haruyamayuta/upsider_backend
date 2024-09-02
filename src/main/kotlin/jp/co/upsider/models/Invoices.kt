package jp.co.upsider.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime


object Invoices : LongIdTable("Invoices") {
    val companyId = long("company_id")
    val clientId = long("client_id")
    val issueDate = date("issue_date")
    val paymentAmount = integer("payment_amount")
    val fee = integer("fee")
    val feeRate = double("fee_rate")
    val tax = integer("tax")
    val taxRate = double("tax_rate")
    val billingAmount = integer("billing_amount")
    val paymentDueDate = date("payment_due_date")
    val status = varchar("status", 10)
    val createdAt = datetime("created_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }
    val updatedAt = datetime("updated_at").clientDefault { ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime() }

    //指定した時間でデータを取得
    fun fetchByPeriodIdAndCompanyId(startDate: LocalDate, endDate: LocalDate, companyId:Long): List<Invoice> {
        return transaction {
            Invoice.find { paymentDueDate.between(startDate, endDate).and(Invoices.companyId.eq(companyId)) }
                .toList()
        }
    }
}

interface IInvoice {
    var companyId: Long
    var clientId: Long
    var issueDate: LocalDate
    var paymentAmount: Int
    var fee: Int
    var feeRate: Double
    var tax: Int
    var taxRate: Double
    var billingAmount: Int
    var paymentDueDate: LocalDate
    var status: String
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime
}

class Invoice(id: EntityID<Long>): LongEntity(id), IInvoice {
    companion object : LongEntityClass<Invoice>(Invoices)

    override var companyId by Invoices.companyId
    override var clientId by Invoices.clientId
    override var issueDate by Invoices.issueDate
    override var paymentAmount by Invoices.paymentAmount
    override var fee by Invoices.fee
    override var feeRate by Invoices.feeRate
    override var tax by Invoices.tax
    override var taxRate by Invoices.taxRate
    override var billingAmount by Invoices.billingAmount
    override var paymentDueDate by Invoices.paymentDueDate
    override var status by Invoices.status
    override var createdAt by Invoices.createdAt
    override var updatedAt by Invoices.updatedAt
}