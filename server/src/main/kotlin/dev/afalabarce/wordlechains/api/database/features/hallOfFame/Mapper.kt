package dev.afalabarce.wordlechains.api.database.features.hallOfFame

import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.models.HallOfFame
import kotlinx.datetime.LocalDate

fun hallOfFameDaoToModel(hallOfFameDao: HallOfFameDao) = HallOfFame(
    id = hallOfFameDao.id.value,
    playerNick = hallOfFameDao.playerNick,
    playerCountry = hallOfFameDao.playerCountry,
    lastPlayDate = LocalDate.fromEpochDays(hallOfFameDao.lastPlayDate.toInt()),
    wordsCount1 = hallOfFameDao.wordsCount1 ?: 0L,
    wordsCount2 = hallOfFameDao.wordsCount2 ?: 0L,
    wordsCount3 = hallOfFameDao.wordsCount3 ?: 0L,
    wordsCount4 = hallOfFameDao.wordsCount4 ?: 0L,
    wordsCount5 = hallOfFameDao.wordsCount5 ?: 0L,
    wordsCount6 = hallOfFameDao.wordsCount6 ?: 0L,
    score = (hallOfFameDao.wordsCount1 ?: 0L) * 6 +
            (hallOfFameDao.wordsCount2 ?: 0L) * 5 +
            (hallOfFameDao.wordsCount3 ?: 0L) * 4 +
            (hallOfFameDao.wordsCount4 ?: 0L) * 3 +
            (hallOfFameDao.wordsCount5 ?: 0L) * 2 +
            (hallOfFameDao.wordsCount6 ?: 0L)
    )