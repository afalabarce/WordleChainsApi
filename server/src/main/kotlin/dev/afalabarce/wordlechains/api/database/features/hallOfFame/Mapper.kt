package dev.afalabarce.wordlechains.api.database.features.hallOfFame

import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.models.HallOfFame
import kotlinx.datetime.LocalDate

fun hallOfFameDaoToModel(hallOfFameDao: HallOfFameDao) = HallOfFame(
    id = hallOfFameDao.id.value,
    playerNick = hallOfFameDao.playerNick,
    playerCountry = hallOfFameDao.playerCountry,
    lastPlayDate = LocalDate.fromEpochDays(hallOfFameDao.lastPlayDate.toInt()),
    wordsCount1 = hallOfFameDao.wordsCount1,
    wordsCount2 = hallOfFameDao.wordsCount2,
    wordsCount3 = hallOfFameDao.wordsCount3,
    wordsCount4 = hallOfFameDao.wordsCount4,
    wordsCount5 = hallOfFameDao.wordsCount5,
    wordsCount6 = hallOfFameDao.wordsCount6,
    score = hallOfFameDao.wordsCount1 * 6 +
            hallOfFameDao.wordsCount2 * 5 +
            hallOfFameDao.wordsCount3 * 4 +
            hallOfFameDao.wordsCount4 * 3 +
            hallOfFameDao.wordsCount5 * 2 +
            hallOfFameDao.wordsCount6
    )