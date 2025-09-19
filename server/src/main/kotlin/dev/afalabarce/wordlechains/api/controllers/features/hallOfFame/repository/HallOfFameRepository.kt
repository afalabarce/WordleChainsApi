package dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.repository

import dev.afalabarce.wordlechains.api.controllers.features.hallOfFame.models.HallOfFame
import dev.afalabarce.wordlechains.api.database.common.suspendTransaction
import dev.afalabarce.wordlechains.api.database.features.hallOfFame.ExposedHallOfFame
import dev.afalabarce.wordlechains.api.database.features.hallOfFame.HallOfFameDao
import dev.afalabarce.wordlechains.api.database.features.hallOfFame.hallOfFameDaoToModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Database.getHallOfFame(): List<HallOfFame> = suspendTransaction {
    HallOfFameDao.all().map(::hallOfFameDaoToModel).toList()
}

suspend fun Database.isNickAvailable(playerNick: String): Boolean = suspendTransaction {
    HallOfFameDao.find { ExposedHallOfFame.playerNick eq playerNick }.empty()
}

suspend fun Database.updateHallOfFame(receivedDeviceId: String?, hallOfFame: HallOfFame): Boolean = suspendTransaction {
    val currentRecord = HallOfFameDao.find {
        ExposedHallOfFame.playerNick eq hallOfFame.playerNick
    }
    try {
        if (receivedDeviceId?.isNotEmpty() == true){
            if (currentRecord.empty()) {
                HallOfFameDao.new {
                    playerNick = hallOfFame.playerNick
                    deviceId = receivedDeviceId
                    playerCountry = hallOfFame.playerCountry
                    lastPlayDate = hallOfFame.lastPlayDate.toEpochDays()
                    wordsCount1 = hallOfFame.wordsCount1
                    wordsCount2 = hallOfFame.wordsCount2
                    wordsCount3 = hallOfFame.wordsCount3
                    wordsCount4 = hallOfFame.wordsCount4
                    wordsCount5 = hallOfFame.wordsCount5
                    wordsCount6 = hallOfFame.wordsCount6
                }
            } else {

                currentRecord.first().apply {
                    if (deviceId == receivedDeviceId) {
                        playerCountry = hallOfFame.playerCountry
                        lastPlayDate = hallOfFame.lastPlayDate.toEpochDays()
                        wordsCount1 = hallOfFame.wordsCount1
                        wordsCount2 = hallOfFame.wordsCount2
                        wordsCount3 = hallOfFame.wordsCount3
                        wordsCount4 = hallOfFame.wordsCount4
                        wordsCount5 = hallOfFame.wordsCount5
                        wordsCount6 = hallOfFame.wordsCount6
                    }else{
                        throw IllegalArgumentException("Device id does not match")
                    }
                }
            }
            true
        }else{
            false
        }
    }catch (_: Exception){
        false
    }
}