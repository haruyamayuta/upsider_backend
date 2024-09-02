package jp.co.upsider.entities.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class FetchInvoicesResponse (
    @SerialName("client_name") val clientName: String,
    @SerialName("issue_data") val issueData: String,
    @SerialName("payment_amount") val paymentAmount: Int,
    @SerialName("fee") val fee: Int,
    @SerialName("fee_rate") val feeRate: Double,
    @SerialName("tax") val tax: Int,
    @SerialName("tax_rate") val taxRate: Double,
    @SerialName("billing_amount") val billingAmount: Int,
    @SerialName("payment_due_date") val paymentDueDate: String,
    @SerialName("status") val status: String
)