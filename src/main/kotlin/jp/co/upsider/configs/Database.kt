package jp.co.upsider.configs

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import jp.co.upsider.Env
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun Application.configureDatabase() {
    if(environment.config.property("ktor.environment").getString() != "test"){
        val config = HikariConfig()
        config.jdbcUrl = "${Env.dbUrl.trimEnd("/"[0])}/${Env.dbName}"
        config.username = Env.dbUser
        config.password = Env.dbPass
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        TransactionManager.defaultDatabase = Database.connect(HikariDataSource(config))
    }
}