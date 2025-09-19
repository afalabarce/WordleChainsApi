package dev.afalabarce.wordlechains.api.common

import dev.afalabarce.wordlechains.api.controllers.features.countries.getCountries
import dev.afalabarce.wordlechains.api.controllers.features.dailyGame.getDailyGame
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.getHallOfFame
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.nickAvailable
import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.updateHallOfFame
import dev.afalabarce.wordlechains.api.controllers.features.words.availableLanguages
import dev.afalabarce.wordlechains.api.controllers.features.words.getWords
import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureRouting(database: Database){
    routing {
        if (environment.config.config("ktor.application").toMap()["debug"] == "true") {
            swaggerUI(
                path = "/swagger",
                swaggerFile = environment.config.config("ktor.application").toMap()["swaggerPath"].toString()
            )
        }

        get(path = "/v1/words/{language?}") {
            call.getWords(database)
        }

        get(path = "/v1/availableLanguages") {
            call.availableLanguages(database)
        }

        get(path = "/v1/dailyGame/{dailyGame}/{language}") {
            call.getDailyGame(database)
        }

        get(path = "/v1/hallOfFame") {
            call.getHallOfFame(database)
        }

        get(path = "/v1/nickAvailable/{playerNick}") {
            call.nickAvailable(database)
        }

        post(path = "/v1/hallOfFame") {

            call.updateHallOfFame(database)
        }

        get(path = "/v1/countries") {
            call.getCountries(database)
        }
    }
}