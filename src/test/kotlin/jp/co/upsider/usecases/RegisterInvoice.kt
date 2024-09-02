package jp.co.upsider.usecases

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.getOrElse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import jp.co.upsider.entities.CustomError
import jp.co.upsider.entities.Errors
import jp.co.upsider.models.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate


class RegisterInvoice: DescribeSpec() {
    init{
        // テスト用データベースに接続
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.create( Companies, Users, Clients, Invoices)
            val company = Company.new {
                name = "test_company"
                postalCode = "1234567"
                address = "test_address"
                phoneNumber = "09012345678"
            }

            User.new {
                uuid = "test_uuid"
                companyId = company.id.value
                name = "test_name"
                email = "test@gmail.com"
                password = "test_password"
            }

            Client.new{
                companyId = company.id.value
                corporateName = "test_corporate_name"
                representativeName = "test_representative_name"
                phoneNumber = "09012345678"
                postalCode = "1234567"
                address = "test_address"
            }
        }

        describe("請求書登録API"){
            it("請求書を登録する"){
                val result = RegisterInvoices.execute(
                    RegisterInvoices.Input(
                        userId = 1,
                        clientId = 1,
                        paymentAmount = 10000,
                        paymentDueDate = LocalDate.parse("2021-01-10"),
                        issueData = LocalDate.parse("2021-01-01"),
                        status = "未処理"
                    )
                ).getOrElse {
                    throw Exception(it.message)
                }

                //期待される結果
                val expectedResult = RegisterInvoices.Output(
                    clientName = "test_corporate_name",
                    issueData = "2021-01-01",
                    paymentAmount = 10000,
                    fee = 400,
                    feeRate = 0.04,
                    tax = 40,
                    taxRate = 0.1,
                    billingAmount = 10440,
                    paymentDueDate = "2021-01-10",
                    status = "未処理"
                )

                //結果の検証
                result shouldBe expectedResult
            }

            it("user情報が存在しない場合のエラー確認"){
                val result = RegisterInvoices.execute(
                    RegisterInvoices.Input(
                        userId = 100,
                        clientId = 1,
                        paymentAmount = 10000,
                        paymentDueDate = LocalDate.parse("2021-01-10"),
                        issueData = LocalDate.parse("2021-01-01"),
                        status = "未処理"
                    )
                )

                result shouldBe Err(Errors.USER_NOT_FOUND.value)
            }

            it("client情報が存在しない場合のエラー確認"){
                val result = RegisterInvoices.execute(
                    RegisterInvoices.Input(
                        userId = 1,
                        clientId = 100,
                        paymentAmount = 10000,
                        paymentDueDate = LocalDate.parse("2021-01-10"),
                        issueData = LocalDate.parse("2021-01-01"),
                        status = "未処理"
                    )
                )

                result shouldBe Err(Errors.CLIENT_NOT_FOUND.value)
            }

        }

    }
}