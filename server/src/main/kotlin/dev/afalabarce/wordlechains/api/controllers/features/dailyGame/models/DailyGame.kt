package dev.afalabarce.wordlechains.api.controllers.features.dailyGame.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DailyGame(
    val dailyId: Int,
    val date: LocalDate,
    val language: String,
    val words: List<DailyGameWord>
)
