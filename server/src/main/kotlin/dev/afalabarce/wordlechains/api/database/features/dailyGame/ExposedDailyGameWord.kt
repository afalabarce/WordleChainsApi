package dev.afalabarce.wordlechains.api.database.features.dailyGame

import dev.afalabarce.wordlechains.api.database.features.words.ExposedWord
import dev.afalabarce.wordlechains.api.database.features.words.WordDao
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ExposedDailyGameWord: IntIdTable(
    name = "daily_game_words",
    columnName = "daily_game_word_id"
) {
    val dailyId = reference(name = "daily_id", refColumn = ExposedDailyGame.id)
    val wordId = reference("word_id", refColumn = ExposedWord.id)
    val linkedWordId = reference("linked_word_id", refColumn = ExposedWord.id)
    val linkingPosition = integer(name = "linking_position")
    val linkedWordPosition = integer(name = "linked_word_position")

    init {
        foreignKey(
            dailyId to ExposedDailyGame.id,
            wordId to ExposedWord.id,
            linkedWordId to ExposedWord.id
        )
    }
}

class DailyGameWordDao(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<DailyGameWordDao>(ExposedDailyGameWord)
    var dailyId by ExposedDailyGameWord.dailyId
    var word: WordDao by WordDao referencedOn ExposedDailyGameWord.wordId
    var linkedWord: WordDao by WordDao referencedOn ExposedDailyGameWord.linkedWordId
    var linkingPosition by ExposedDailyGameWord.linkingPosition
    var linkedWordPosition by ExposedDailyGameWord.linkedWordPosition
}