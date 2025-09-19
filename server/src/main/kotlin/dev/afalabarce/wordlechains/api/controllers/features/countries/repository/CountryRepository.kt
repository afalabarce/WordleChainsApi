package dev.afalabarce.wordlechains.api.controllers.features.countries.repository

import dev.afalabarce.wordlechains.api.controllers.features.countries.models.Country
import dev.afalabarce.wordlechains.api.database.common.suspendTransaction
import dev.afalabarce.wordlechains.api.database.features.countries.CountryDao
import dev.afalabarce.wordlechains.api.database.features.countries.countryDaoToModel
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Database.getAllCountries(): List<Country> = suspendTransaction {
    CountryDao.all().map(::countryDaoToModel)
}