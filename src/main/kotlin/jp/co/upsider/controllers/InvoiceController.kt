package jp.co.upsider.controllers

import com.github.michaelbull.result.getOrElse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import jp.co.upsider.entities.request.RegisterInvoiceRequest
import jp.co.upsider.entities.response.RegisterInvoiceResponse
import jp.co.upsider.usecases.FetchInvoices
import jp.co.upsider.usecases.RegisterInvoices
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate

class InvoiceController (private val call: ApplicationCall){
    suspend fun register(){
        //user情報を取得
        //本来はtoken情報からuser情報を取得する
        val userId = 1
        val params = call.receive<RegisterInvoiceRequest>()

        //バリデーション
        val paymentDueDate: LocalDate
        val issueData: LocalDate
        try {
            paymentDueDate = LocalDate.parse(params.paymentDueDate)
            issueData = LocalDate.parse(params.issueData)
        } catch (e: ParseException) {
            return call.respond(HttpStatusCode.BadRequest, "startDate and endDate must be in the format yyyy-MM-dd")
        }

        val response = RegisterInvoices.execute(RegisterInvoices.Input(
            paymentAmount = params.paymentAmount,
            paymentDueDate = paymentDueDate,
            status = params.status,
            issueData = issueData,
            clientId = params.clientId,
            userId = userId.toLong()
        )).getOrElse {
            return call.respond(it.status, it)
        }

        return call.respond(
            RegisterInvoiceResponse(
                clientName = response.clientName,
                issueData = response.issueData,
                paymentAmount = response.paymentAmount,
                fee = response.fee,
                feeRate = response.feeRate,
                tax = response.tax,
                taxRate = response.taxRate,
                billingAmount = response.billingAmount,
                paymentDueDate = response.paymentDueDate,
                status = response.status
            )
        )
    }

    suspend fun list(){
        //user情報を取得
        //本来はtoken情報からuser情報を取得する
        val userId = 1

        val startDate = call.request.queryParameters["startDate"]
        val endDate = call.request.queryParameters["endDate"]

        //バリデーション
        val startDateParse: LocalDate
        val endDateParse: LocalDate
        if (startDate == null || endDate == null) {
            return call.respond(HttpStatusCode.BadRequest, "startDate and endDate are required")
        }
        try {
            startDateParse = LocalDate.parse(startDate)
            endDateParse = LocalDate.parse(endDate)
        } catch (e: ParseException) {
            return call.respond(HttpStatusCode.BadRequest, "startDate and endDate must be in the format yyyy-MM-dd")
        }

        val response = FetchInvoices.execute(FetchInvoices.Input(
            userId = userId.toLong(),
            startDate = startDateParse,
            endDate = endDateParse
        )).getOrElse {
            return call.respond(it.status, it)
        }

        return call.respond(
            response.map {
                RegisterInvoiceResponse(
                    clientName = it.clientName,
                    issueData = it.issueData.toString(),
                    paymentAmount = it.paymentAmount,
                    fee = it.fee,
                    feeRate = it.feeRate,
                    tax = it.tax,
                    taxRate = it.taxRate,
                    billingAmount = it.billingAmount,
                    paymentDueDate = it.paymentDueDate.toString(),
                    status = it.status
                )
            }
        )
    }
}
