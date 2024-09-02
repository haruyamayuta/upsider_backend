package jp.co.upsider.usecases

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import jp.co.upsider.entities.CustomError
import com.github.michaelbull.result.Result
import jp.co.upsider.entities.Errors
import jp.co.upsider.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Date
import java.time.LocalDate
import kotlin.math.ceil

object RegisterInvoices {
    data class Input(
        val paymentAmount: Int,
        val paymentDueDate: LocalDate,
        val status: String,
        val issueData: LocalDate,
        val clientId: Long,
        val userId: Long
    )

    //invoicesテーブルの情報
    //取引相手の情報
    data class Output(
        val clientName: String,
        val issueData: String,
        val paymentAmount: Int,
        val fee: Int,
        val feeRate: Double,
        val tax: Int,
        val taxRate: Double,
        val billingAmount: Int,
        val paymentDueDate: String,
        val status: String
    )


    fun execute(params: Input): Result<Output, CustomError> {
        val feeRateValue = 0.04 // 手数料
        val taxRateValue = 0.1  // 消費税

        //手数料と税金（小数点は切り上げる）
        val feeValue = ceil(params.paymentAmount * feeRateValue).toInt()
        val taxValue = ceil( feeValue * taxRateValue).toInt()

        val billingAmountValue = params.paymentAmount + feeValue + taxValue
        val user = Users.fetchById(params.userId) ?: return Err(Errors.USER_NOT_FOUND.value)
        val client = Clients.fetchById(params.clientId) ?: return Err(Errors.CLIENT_NOT_FOUND.value)

        var invoice: Invoice? = null
        transaction {
            invoice =
                Invoice.new {
                    companyId = user.companyId
                    clientId = params.clientId
                    issueDate = params.issueData
                    paymentAmount = params.paymentAmount
                    feeRate = feeRateValue
                    fee = feeValue
                    taxRate = taxRateValue
                    tax = taxValue
                    billingAmount = billingAmountValue
                    paymentDueDate = params.paymentDueDate
                    status = params.status
                }
        }

        return Ok(
            Output(
                clientName = client.corporateName,
                issueData = invoice!!.issueDate.toString(),
                fee = invoice!!.fee,
                feeRate = invoice!!.feeRate,
                tax = invoice!!.tax,
                taxRate = invoice!!.taxRate,
                billingAmount = invoice!!.billingAmount,
                paymentAmount = invoice!!.paymentAmount,
                paymentDueDate = invoice!!.paymentDueDate.toString(),
                status = invoice!!.status
        ))
    }
}