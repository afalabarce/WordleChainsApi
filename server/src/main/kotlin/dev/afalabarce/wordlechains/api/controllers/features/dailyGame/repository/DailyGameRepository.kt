package dev.afalabarce.wordlechains.api.controllers.features.dailyGame.repository

import dev.afalabarce.wordlechains.api.controllers.features.dailyGame.models.DailyGame
import dev.afalabarce.wordlechains.api.database.common.suspendTransaction
import dev.afalabarce.wordlechains.api.database.features.dailyGame.DailyGameDao
import dev.afalabarce.wordlechains.api.database.features.dailyGame.ExposedDailyGame
import dev.afalabarce.wordlechains.api.database.features.dailyGame.dailyGameDaoToModel
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Database.getDailyGame(date: LocalDate, language: String): DailyGame? = suspendTransaction {
    DailyGameDao.find {
        ExposedDailyGame.date eq date.toEpochDays() and(
            ExposedDailyGame.language eq language
        )
    }.firstOrNull()?.let(::dailyGameDaoToModel)
}