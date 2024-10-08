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
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate


class FetchInvoicesTest: DescribeSpec() {
    init {
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

            val client = Client.new{
                companyId = company.id.value
                corporateName = "test_corporate_name"
                representativeName = "test_representative_name"
                phoneNumber = "09012345678"
                postalCode = "1234567"
                address = "test_address"
            }

            Invoice.new {
                companyId = company.id.value
                clientId = client.id.value
                issueDate = LocalDate.parse("2021-01-01")
                paymentAmount = 10000
                fee = 400
                feeRate = 0.04
                tax = 40
                taxRate = 0.1
                billingAmount = 10440
                paymentDueDate = LocalDate.parse("2021-01-10")
                status = "未処理"
            }
        }

        describe("請求書取得APIテスト") {
            it("正しい請求書を取得する") {

                val result = FetchInvoices.execute(
                    FetchInvoices.Input(
                        userId = 1,
                        startDate = LocalDate.parse("2021-01-01"),
                        endDate = LocalDate.parse("2121-01-01")
                    )
                ).getOrElse {
                    throw Exception("error")
                }

                //期待される結果
                val expectedResult = FetchInvoices.Output(
                    clientName = "test_corporate_name",
                    issueData = LocalDate.parse("2021-01-01"),
                    paymentAmount = 10000,
                    fee = 400,
                    feeRate = 0.04,
                    tax = 40,
                    taxRate = 0.1,
                    billingAmount = 10440,
                    paymentDueDate = LocalDate.parse("2021-01-10"),
                    status = "未処理"
                )

                // テスト結果確認
                result shouldBe listOf(expectedResult)
            }

            it("user情報が存在しない場合のエラー確認"){
                val result = FetchInvoices.execute(
                    FetchInvoices.Input(
                        userId = 100,
                        startDate = LocalDate.parse("2021-01-01"),
                        endDate = LocalDate.parse("2121-01-01")
                    )
                )

                result shouldBe Err(Errors.USER_NOT_FOUND.value)
            }

            it("取得する請求書データがない場合"){
                //invoiceのデータ削除
                transaction {
                    val company = Company.new {
                        name = "test2_company"
                        postalCode = "1234567"
                        address = "test2_address"
                        phoneNumber = "09012345678"
                    }

                    User.new {
                        uuid = "test2_uuid"
                        companyId = company.id.value
                        name = "test2_name"
                        email = "test2@gmail.com"
                        password = "test2_password"
                    }
                }

                val result = FetchInvoices.execute(
                    FetchInvoices.Input(
                        userId = 2,
                        startDate = LocalDate.parse("2021-01-01"),
                        endDate = LocalDate.parse("2121-01-01")
                    )
                ).getOrElse {
                    throw Exception("error")
                }

                // テスト結果確認
                result shouldBe listOf()
            }
        }
    }
}
