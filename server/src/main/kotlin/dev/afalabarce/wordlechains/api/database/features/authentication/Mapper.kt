package dev.afalabarce.wordlechains.api.database.features.authentication

import dev.afalabarce.wordlechains.api.controllers.features.authentication.models.DeveloperApiKey

fun developerApiKeyDaoToModel(dao: DeveloperApiKeyDao): DeveloperApiKey = DeveloperApiKey(
    developerEmail = dao.developerEmail,
    developerApiKey = dao.developerApiKey.value.toString()

)