package dev.afalabarce.wordlechains.api.database.common

import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun Application.databaseConnect(): Database? {
    return try {
        val databaseSettings = environment.config.config("ktor.database").toMap()
        val server = databaseSettings["server"]
        val database = databaseSettings["databaseName"]
        val port = databaseSettings["port"]
        val user = databaseSettings["userName"]
        val password = databaseSettings["password"]

        Database.connect(
            url = "jdbc:postgresql://$server:$port/$database",
            user = user?.toString().orEmpty(),
            password = password?.toString().orEmpty()
        )
    }catch (e: Exception) {
        throw e
    }
}