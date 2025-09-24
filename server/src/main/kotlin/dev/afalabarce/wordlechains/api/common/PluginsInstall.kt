package dev.afalabarce.wordlechains.api.common

import dev.afalabarce.wordlechains.api.controllers.features.authentication.repository.developerExists
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.installAuthentication(database: Database){
    val privateKeyPath = environment.config.property("ktor.application.security.privateKeyPath").getString()

    install(Authentication){
        bearer {
            realm = "Wordle Chains"
            authenticate { tokenCredential ->
                if (tokenCredential.token.isNotEmpty()) {
                    val encryptedCredentials = tokenCredential.token
                    encryptedCredentials.decryptRSA(
                        privateKeyPath = privateKeyPath
                    )?.let { decryptedCredentials ->
                        val (developerEmail, apiKey) = decryptedCredentials.split(":")
                        database.developerExists(developerEmail, apiKey.replace("\n", ""))
                    } ?: run {
                        null
                    }
                } else {
                    null
                }
            }
        }
    }
}
fun Application.installPlugins() {
    install(io.ktor.server.resources.Resources)
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}