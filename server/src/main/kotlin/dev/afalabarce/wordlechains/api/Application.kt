package dev.afalabarce.wordlechains.api

import dev.afalabarce.wordlechains.api.common.configureRouting
import dev.afalabarce.wordlechains.api.common.installPlugins
import dev.afalabarce.wordlechains.api.database.common.databaseConnect
import io.ktor.server.application.Application


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    installPlugins()
    databaseConnect()?.let { database ->
        configureRouting(database)
    } ?: run {
        throw IllegalStateException("Database Exception")
    }

}