package dev.afalabarce.wordlechains.api.controllers.features.authentication.repository

import dev.afalabarce.wordlechains.api.controllers.features.authentication.models.DeveloperApiKey
import dev.afalabarce.wordlechains.api.database.common.suspendTransaction
import dev.afalabarce.wordlechains.api.database.features.authentication.DeveloperApiKeyDao
import dev.afalabarce.wordlechains.api.database.features.authentication.ExposedDeveloperApiKey
import dev.afalabarce.wordlechains.api.database.features.authentication.developerApiKeyDaoToModel
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
suspend fun Database.developerExists(developerEmail: String, developerApiKey: String): DeveloperApiKey? =
    suspendTransaction {
        DeveloperApiKeyDao.find {
            (ExposedDeveloperApiKey.developerEmail eq developerEmail) and (ExposedDeveloperApiKey.id eq
                    UUID.fromString(developerApiKey))
        }.map(::developerApiKeyDaoToModel).firstOrNull()
    }