package jp.co.upsider

import io.github.cdimascio.dotenv.dotenv


object Env {
    private val dotenv = dotenv {
        ignoreIfMalformed = true
        ignoreIfMissing = true
    }

    val dbUser = dotenv["DB_USER"]?:"root"
    val dbPass = dotenv["DB_PASS"]?:"pass"
    val dbUrl = dotenv["DB_URL"]?:"jdbc:mysql://localhost:3306"
    val dbName = dotenv["DB_NAME"]?:"invoices"
}