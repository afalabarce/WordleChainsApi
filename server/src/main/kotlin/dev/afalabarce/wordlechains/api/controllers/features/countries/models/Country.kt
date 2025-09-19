package dev.afalabarce.wordlechains.api.controllers.features.countries.models

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val countryId: String,
    val countryName: String,
    val flagUrl: String
)