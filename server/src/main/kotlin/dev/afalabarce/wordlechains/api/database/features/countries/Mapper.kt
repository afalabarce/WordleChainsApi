package dev.afalabarce.wordlechains.api.database.features.countries

import dev.afalabarce.wordlechains.api.controllers.features.countries.models.Country

fun countryDaoToModel(dao: CountryDao): Country = Country(
    countryId = dao.countryId.value,
    countryName = dao.countryName,
    flagUrl = dao.countryFlag
)