package jp.co.upsider.entities.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotNull
import org.valiktor.validate


@Serializable
class RegisterInvoiceRequest(
    @SerialName("payment_amount") val paymentAmount: Int,
    @SerialName("payment_due_data") val paymentDueDate: String,
    @SerialName("issue_data") val issueData: String,
    @SerialName("client_id") val clientId: Long,
    @SerialName("status") val status: String,
){
    init{
        validate(this){
            validate(RegisterInvoiceRequest::paymentAmount).isNotNull()
            validate(RegisterInvoiceRequest::paymentDueDate).isNotNull()
            validate(RegisterInvoiceRequest::status).isNotNull()
        }
    }
}