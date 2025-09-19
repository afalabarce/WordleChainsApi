package dev.afalabarce.wordlechains.api.database.features.words

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ExposedWord: IntIdTable(name = "words") {
    var word = varchar(name = "word", length = 5)
    var definition = text(name = "definition")
    var language = varchar(name = "language", length = 10)
}

class WordDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<WordDao>(ExposedWord)

    var wordId by ExposedWord.id
    var word by ExposedWord.word
    var definition by ExposedWord.definition
    var language by ExposedWord.language
}