package dev.afalabarce.wordlechains.api.controllers.features.words

import dev.afalabarce.wordlechains.api.controllers.features.words.models.Word
import dev.afalabarce.wordlechains.api.controllers.features.words.repository.addNewWord
import dev.afalabarce.wordlechains.api.controllers.features.words.repository.availableLanguages
import dev.afalabarce.wordlechains.api.controllers.features.words.repository.getAllWords
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import org.apache.http.HttpStatus
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

suspend fun RoutingCall.addNewWord(database: Database, newWord: Word){

    respond(
        status =  if(database.addNewWord(newWord)) HttpStatusCode.OK else HttpStatusCode.BadRequest,
        message = ""

    )
}