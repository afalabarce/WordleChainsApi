package dev.afalabarce.wordlechains.api.controllers.features.hallOfFame

import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.models.HallOfFame
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.repository.getHallOfFame
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.repository.isNickAvailable
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.repository.updateHallOfFame
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun RoutingCall.getHallOfFame(database: Database){
    respond(database.getHallOfFame())
}

suspend fun RoutingCall.updateHallOfFame(database: Database){
    val data: String =  this.receive()
    val deviceId: String? = this.request.headers["Device-Id"]
    val newHallOfFame = Json.decodeFromString<HallOfFame>(data)
    respond(
        status = if (database.updateHallOfFame(deviceId, newHallOfFame))
            HttpStatusCode.OK
        else
            HttpStatusCode.Conflict,
        message = ""
    )
}

suspend fun RoutingCall.nickAvailable(database: Database) {
    this.parameters["playerNick"]?.trim()?.ifEmpty { null }?.let { playerNick ->
        respond(
            status = if (database.isNickAvailable(playerNick = playerNick))
                HttpStatusCode.OK
            else
                HttpStatusCode.Conflict,
            message = ""
        )
    } ?: run {
        respond(status = HttpStatusCode.BadRequest, message = "Player nick is required")
    }
}