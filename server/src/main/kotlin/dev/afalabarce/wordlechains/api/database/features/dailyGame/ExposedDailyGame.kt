package dev.afalabarce.wordlechains.api.database.features.dailyGame

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.SizedIterable

object ExposedDailyGame: IntIdTable(
    name = "daily_game",
    columnName = "daily_id"
){
    val date = long(name = "date")
    val language = varchar(name = "language", length = 10)
}

class DailyGameDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<DailyGameDao>(ExposedDailyGame)
    var date by ExposedDailyGame.date
    var language by ExposedDailyGame.language
    val words: SizedIterable<DailyGameWordDao> by DailyGameWordDao referrersOn ExposedDailyGameWord.dailyId

}