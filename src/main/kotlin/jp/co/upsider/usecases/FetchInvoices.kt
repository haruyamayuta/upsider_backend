package jp.co.upsider.usecases

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.co.upsider.entities.CustomError
import jp.co.upsider.entities.Errors
import jp.co.upsider.models.Clients
import jp.co.upsider.models.Invoices
import jp.co.upsider.models.Users
import java.sql.Date
import java.time.LocalDate

object FetchInvoices {
    data class Input(
        val userId: Long,
        val startDate: LocalDate,
        val endDate: LocalDate
    )

    data class Output(
        val clientName: String,
        val issueData: LocalDate,
        val paymentAmount: Int,
        val fee: Int,
        val feeRate: Double,
        val tax: Int,
        val taxRate: Double,
        val billingAmount: Int,
        val paymentDueDate: LocalDate,
        val status: String
    )

    fun execute(params: Input): Result<List<Output>, CustomError> {
        //user情報取得
        val user = Users.fetchById(params.userId) ?: return Err(Errors.USER_NOT_FOUND.value)

        //指定した時間でデータを取得
        //startDateとendDateをDate型に変換
        val invoices = Invoices.fetchByPeriodIdAndCompanyId(params.startDate, params.endDate, user.companyId)
        val clientId = invoices.map { it.clientId }.distinct()
        val clients = Clients.fetchByIds(clientId)

        return Ok(invoices.map {
            Output(
                clients.find { client -> client.id.value == it.clientId }?.corporateName ?: "",
                it.issueDate,
                it.paymentAmount,
                it.fee,
                it.feeRate,
                it.tax,
                it.taxRate,
                it.billingAmount,
                it.paymentDueDate,
                it.status
            )
        })
    }
}