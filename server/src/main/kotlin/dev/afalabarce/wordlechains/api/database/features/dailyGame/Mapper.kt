package dev.afalabarce.wordlechains.api.database.features.dailyGame

import dev.afalabarce.wordlechains.api.controllers.features.dailyGame.models.DailyGame
import dev.afalabarce.wordlechains.api.controllers.features.dailyGame.models.DailyGameWord
import kotlinx.datetime.LocalDate

fun dailyGameDaoToModel(dailyGameDao: DailyGameDao): DailyGame = DailyGame(
    dailyId = dailyGameDao.id.value,
    date = LocalDate.fromEpochDays(dailyGameDao.date.toInt()) ,
    language = dailyGameDao.language,
    words = dailyGameDao.words.map(::dailyGameWordDaoToModel)
)

fun dailyGameWordDaoToModel(dailyGameWordDao: DailyGameWordDao): DailyGameWord = DailyGameWord(
    dailyGameWordId = dailyGameWordDao.id.value,
    dailyId = dailyGameWordDao.dailyId.value,
    wordId = dailyGameWordDao.word.wordId.value,
    linkedWordId = dailyGameWordDao.linkedWord?.value,
    linkingPosition = dailyGameWordDao.linkingPosition,
    linkedWordPosition = dailyGameWordDao.linkedWordPosition
)