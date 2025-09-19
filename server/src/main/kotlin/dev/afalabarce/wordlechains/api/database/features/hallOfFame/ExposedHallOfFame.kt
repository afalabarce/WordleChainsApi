package dev.afalabarce.wordlechains.api.database.features.hallOfFame

import dev.afalabarce.wordlechains.api.database.features.countries.ExposedCountry
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ExposedHallOfFame: IntIdTable("hall_of_fame") {
    val playerNick = varchar("player_nick", 100)
    val deviceId = varchar("device_id", 255)
    val playerCountry = varchar(name = "player_country_id", length = 10)
    val lastPlayDate = long("last_play_date")
    val wordsCount1 = long("words_count_1")
    val wordsCount2 = long("words_count_2")
    val wordsCount3 = long("words_count_3")
    val wordsCount4 = long("words_count_4")
    val wordsCount5 = long("words_count_5")
    val wordsCount6 = long("words_count_6")

    init {
        foreignKey(playerCountry to ExposedCountry.id)
    }
}

class HallOfFameDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<HallOfFameDao>(ExposedHallOfFame)
    var playerNick by ExposedHallOfFame.playerNick
    var deviceId by ExposedHallOfFame.deviceId
    var playerCountry by ExposedHallOfFame.playerCountry
    var lastPlayDate by ExposedHallOfFame.lastPlayDate
    var wordsCount1 by ExposedHallOfFame.wordsCount1
    var wordsCount2 by ExposedHallOfFame.wordsCount2
    var wordsCount3 by ExposedHallOfFame.wordsCount3
    var wordsCount4 by ExposedHallOfFame.wordsCount4
    var wordsCount5 by ExposedHallOfFame.wordsCount5
    var wordsCount6 by ExposedHallOfFame.wordsCount6
}
