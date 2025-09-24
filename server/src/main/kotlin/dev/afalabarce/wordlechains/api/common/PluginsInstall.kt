package dev.afalabarce.wordlechains.api.common

import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json

fun Application.installAuthentication(tokenValidation: (String) -> Map<String, String>){
    install(Authentication){
        bearer (name = "auth-bearer") {
            realm = "Wordle Chains"
            authenticate { tokenCredential ->
                if (tokenCredential.token.isNotEmpty()) {
                    val encryptedCredentials = tokenCredential.token

                    tokenValidation(encryptedCredentials)
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