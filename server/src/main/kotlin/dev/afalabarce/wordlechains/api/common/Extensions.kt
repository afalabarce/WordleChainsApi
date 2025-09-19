package dev.afalabarce.wordlechains.api.common

fun String?.isValidDate() = try {
    val dateRegex = Regex("^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    this != null && dateRegex.matches(this)
}catch (_: Exception) {
    false
}