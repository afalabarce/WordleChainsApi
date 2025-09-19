package dev.afalabarce.wordlechains.api.database.features.words

import dev.afalabarce.wordlechains.api.controllers.features.words.models.Word

fun wordDaoToModel(dao: WordDao): Word = Word(
    wordId = dao.wordId.value,
    word = dao.word,
    definition = dao.definition,
    language = dao.language

)