package jp.co.upsider

import io.ktor.server.application.*
import jp.co.upsider.configs.configureDatabase
import jp.co.upsider.configs.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureDatabase()
}
