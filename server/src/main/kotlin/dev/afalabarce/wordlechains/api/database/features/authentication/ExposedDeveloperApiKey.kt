package dev.afalabarce.wordlechains.api.database.features.authentication

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import java.util.UUID

object ExposedDeveloperApiKey: IdTable<UUID>("api_keys") {
    override val id: Column<EntityID<UUID>> = uuid("api_key").entityId()

    val developerEmail = text("developer")
}

class DeveloperApiKeyDao(id: EntityID<UUID>): Entity<UUID>(id) {
    companion object : EntityClass<UUID, DeveloperApiKeyDao>(ExposedDeveloperApiKey)

    var developerApiKey by ExposedDeveloperApiKey.id
    var developerEmail by ExposedDeveloperApiKey.developerEmail
}