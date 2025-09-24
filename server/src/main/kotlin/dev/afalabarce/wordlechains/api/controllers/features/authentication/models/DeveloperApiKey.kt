package dev.afalabarce.wordlechains.api.controllers.features.authentication.models

import kotlinx.serialization.Serializable

@Serializable
data class DeveloperApiKey(
    val developerEmail: String,
    val developerApiKey: String,
)
