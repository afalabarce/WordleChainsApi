package dev.afalabarce.wordlechains.api.controllers.features.words

import dev.afalabarce.wordlechains.api.controllers.features.words.repository.availableLanguages
import dev.afalabarce.wordlechains.api.controllers.features.words.repository.getAllWords
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import org.jetbrains.exposed.v1.jdbc.Database


suspend fun RoutingCall.getWords(database: Database){
    respond(
    this.parameters["language"]?.ifEmpty { null }?.let {
        language -> database.getAllWords(language)
    } ?: run {
        database.getAllWords()
    })
}

suspend fun RoutingCall.availableLanguages(database: Database){
    respond(database.availableLanguages())
}