package dev.afalabarce.wordlechains.api.controllers.features.words.repository

import dev.afalabarce.wordlechains.api.controllers.features.words.models.Word
import dev.afalabarce.wordlechains.api.database.common.suspendTransaction
import dev.afalabarce.wordlechains.api.database.features.words.ExposedWord
import dev.afalabarce.wordlechains.api.database.features.words.NewWordDao
import dev.afalabarce.wordlechains.api.database.features.words.WordDao
import dev.afalabarce.wordlechains.api.database.features.words.wordDaoToModel
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Database.getAllWords(): List<Word> = suspendTransaction {
    WordDao.all().map(::wordDaoToModel)
}

suspend fun Database.getAllWords(language: String): List<Word> = suspendTransaction {
    WordDao.find { ExposedWord.language eq language }.map(::wordDaoToModel)
}

suspend fun Database.availableLanguages(): List<String> = suspendTransaction {
    WordDao.all().map { it.language }.distinct()
}

suspend fun Database.addNewWord(newWord: Word): Boolean = suspendTransaction {
    try {
        NewWordDao.new {
            word = newWord.word
            definition = newWord.definition
            language = newWord.language
            isValidWord = 0
        }

        true
    }catch (_: Exception){
        false
    }
}
