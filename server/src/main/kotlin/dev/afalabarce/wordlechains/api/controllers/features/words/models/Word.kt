package dev.afalabarce.wordlechains.api.controllers.features.words.models

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val wordId: Int,
    val word: String,
    val definition: String,
    val language: String
)