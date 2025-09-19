package dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class HallOfFame(
    val id: Int = 0,
    val playerNick: String,
    val deviceId: String = "",
    val playerCountry: String,
    val lastPlayDate: LocalDate,
    val wordsCount1:Long,
    val wordsCount2:Long,
    val wordsCount3:Long,
    val wordsCount4:Long,
    val wordsCount5:Long,
    val wordsCount6:Long,
    val score: Long = 0,
)