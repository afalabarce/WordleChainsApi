package dev.afalabarce.wordlechains.api.common

import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import kotlin.io.encoding.Base64

fun String?.isValidDate() = try {
    val dateRegex = Regex("^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    this != null && dateRegex.matches(this)
}catch (_: Exception) {
    false
}

fun String.decryptRSA(privateKeyPath: String): String? {
    return try {
        val encryptedBytes = Base64.decode(this)
        val keyBytes = Files.readAllBytes(Paths.get(privateKeyPath))
        val pemBytes = Base64.decode(String(keyBytes).split("\n").filter { line ->
            !line.startsWith("-----BEGIN") && !line.startsWith("-----END")
        }.joinToString("").replace("\n", ""))
        val keySpec = PKCS8EncodedKeySpec(pemBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        String(decryptedBytes, Charsets.UTF_8)

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
