package dev.afalabarce.wordlechains.api.controllers.features.dailyGame.models

import kotlinx.serialization.Serializable

@Serializable
data class DailyGameWord(
    val dailyGameWordId: Int,
    val dailyId: Int,
    val wordId: Int,
    val linkedWordId: Int,
    val linkingPosition: Int,
    val linkedWordPosition: Int,
)
