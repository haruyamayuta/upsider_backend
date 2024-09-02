package jp.co.upsider.configs

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jp.co.upsider.controllers.InvoiceController
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        get("/") {
            call.respondText("Hello World!!!!")
        }

        route("/invoices"){
            //請求書登録API
            post("/register"){ InvoiceController(call).register() }
            //請求書一覧API
            get("list") { InvoiceController(call).list() }
        }
    }
}
