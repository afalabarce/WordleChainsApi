package dev.afalabarce.wordlechains.api.controllers.features.countries

import dev.afalabarce.wordlechains.api.controllers.features.countries.repository.getAllCountries
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import org.jetbrains.exposed.v1.jdbc.Database


suspend fun RoutingCall.getCountries(database: Database) {
    respond(database.getAllCountries())
}