package dev.afalabarce.wordlechains.api.database.features.countries

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

object ExposedCountry: IdTable<String>(
    name = "countries",
){
    var countryName = varchar(name = "country_name", length = 100)
    var countryFlag = varchar(name = "flag_url", length = 500)
    override val id = varchar("country_id", 10).entityId()
}

class CountryDao(id: EntityID<String>): Entity<String>(id) {
    companion object : EntityClass<String, CountryDao>(ExposedCountry)

    var countryId by ExposedCountry.id
    var countryName by ExposedCountry.countryName
    var countryFlag by ExposedCountry.countryFlag
}