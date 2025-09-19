package dev.afalabarce.wordlechains.api.controllers.features.dailyGame

import dev.afalabarce.wordlechains.api.common.isValidDate
import dev.afalabarce.wordlechains.api.controllers.features.dailyGame.repository.getDailyGame
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun RoutingCall.getDailyGame(database: Database){
    val dailyGame = this.parameters["dailyGame"]
    val language = this.parameters["language"]

    if (dailyGame.isValidDate() && language.orEmpty().isNotEmpty()) {
        val localDate: LocalDate = LocalDate.parse(dailyGame!!)

        respond(database.getDailyGame(localDate, language!!))
    }else {
        this.respond(status = HttpStatusCode.BadRequest, "Invalid date format")
    }
}