package dev.afalabarce.wordlechains.api.database.features.words

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ExposedNewWord: IntIdTable(name = "new_words") {
    var word = varchar(name = "word", length = 5)
    var definition = text(name = "definition")
    var language = varchar(name = "language", length = 10)
    var isValidWord = integer(name = "is_valid_word")
}

class NewWordDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<NewWordDao>(ExposedNewWord)

    var wordId by ExposedNewWord.id
    var word by ExposedNewWord.word
    var definition by ExposedNewWord.definition
    var language by ExposedNewWord.language
    var isValidWord by ExposedNewWord.isValidWord
}