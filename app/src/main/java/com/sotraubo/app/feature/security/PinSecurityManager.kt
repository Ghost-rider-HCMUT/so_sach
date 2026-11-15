package com.sotraubo.app.feature.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Bộ quản lý mật khẩu 6 chữ số (PIN 6 số) bảo vệ ứng dụng khi mở máy:
 * - Bỏ hoàn toàn bước bắt thiết lập lần đầu: Nếu chưa có mật khẩu tùy chỉnh, sử dụng mật khẩu 6 số mặc định (123456).
 * - Khi người dùng đổi mật khẩu, lưu băm SHA-256 kèm Salt 16 bytes và mã hóa bằng AES-256-GCM trong Android Keystore.
 */
interface PinSecurityStorage {
    fun isPinConfigured(): Boolean
    fun setupNewPin(pin6Digits: String): Boolean
    fun verifyPin(pin6Digits: String): Boolean
    fun changePin(oldPin6Digits: String, newPin6Digits: String): Result<Unit>
    fun isBiometricEnabled(): Boolean = true
    fun setBiometricEnabled(enabled: Boolean) {}
}

class PinSecurityManager(
    private val storageFile: File,
    private val useAndroidKeystore: Boolean = true
) : PinSecurityStorage {

    companion object {
        const val PIN_LENGTH = 6
        const val DEFAULT_PIN = "123456"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "so_trau_bo_pin_aes_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_BITS = 128

        fun create(context: Context): PinSecurityManager {
            val file = File(context.filesDir, "pin_security_store.dat")
            return PinSecurityManager(storageFile = file, useAndroidKeystore = true)
        }

        fun isValidSixDigitPin(pin: String): Boolean {
            return pin.length == PIN_LENGTH && pin.all { it.isDigit() }
        }

        internal fun hashPinWithSalt(pin: String, salt: ByteArray): String {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(salt)
            val hashed = digest.digest(pin.toByteArray(Charsets.UTF_8))
            return hashed.joinToString("") { "%02x".format(it) }
        }
    }

    override fun isPinConfigured(): Boolean {
        val record = readRecord() ?: return false
        return record.first.isNotEmpty() && record.second.isNotEmpty()
    }

    override fun setupNewPin(pin6Digits: String): Boolean {
        if (!isValidSixDigitPin(pin6Digits)) return false
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hashHex = hashPinWithSalt(pin6Digits, salt)
        val saltHex = salt.joinToString("") { "%02x".format(it) }
        return writeRecord(saltHex = saltHex, hashHex = hashHex)
    }

    override fun verifyPin(pin6Digits: String): Boolean {
        if (!isValidSixDigitPin(pin6Digits)) return false
        val record = readRecord()
        if (record == null) return false
        val (saltHex, expectedHashHex) = record
        val salt = hexToBytes(saltHex)
        val actualHashHex = hashPinWithSalt(pin6Digits, salt)
        return MessageDigest.isEqual(
            expectedHashHex.lowercase().toByteArray(Charsets.UTF_8),
            actualHashHex.lowercase().toByteArray(Charsets.UTF_8)
        )
    }

    override fun changePin(oldPin6Digits: String, newPin6Digits: String): Result<Unit> {
        if (!verifyPin(oldPin6Digits)) {
            return Result.failure(IllegalArgumentException("Mật khẩu hiện tại không đúng."))
        }
        if (!isValidSixDigitPin(newPin6Digits)) {
            return Result.failure(IllegalArgumentException("Mật khẩu mới phải gồm đúng 6 chữ số."))
        }
        val saved = setupNewPin(newPin6Digits)
        return if (saved) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("Không thể lưu mật khẩu mới. Vui lòng thử lại."))
        }
    }

    override fun isBiometricEnabled(): Boolean {
        val flagFile = biometricFlagFile() ?: return true
        if (!flagFile.exists()) return true
        return runCatching { flagFile.readText(Charsets.UTF_8).trim() != "false" }.getOrDefault(true)
    }

    override fun setBiometricEnabled(enabled: Boolean) {
        val flagFile = biometricFlagFile() ?: return
        runCatching {
            flagFile.parentFile?.mkdirs()
            flagFile.writeText(enabled.toString(), Charsets.UTF_8)
        }
    }

    private fun biometricFlagFile(): File? {
        return storageFile.parentFile?.let { File(it, "biometric_enabled.flag") }
    }

    private fun writeRecord(saltHex: String, hashHex: String): Boolean {
        return try {
            val payload = "$saltHex:$hashHex:${System.currentTimeMillis()}"
            val bytesToStore = if (useAndroidKeystore) {
                encryptWithKeystore(payload.toByteArray(Charsets.UTF_8))
            } else {
                payload.toByteArray(Charsets.UTF_8)
            }
            storageFile.parentFile?.mkdirs()
            storageFile.writeBytes(bytesToStore)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun readRecord(): Pair<String, String>? {
        if (!storageFile.exists()) return null
        return try {
            val rawBytes = storageFile.readBytes()
            if (rawBytes.isEmpty()) return null
            val plainBytes = if (useAndroidKeystore) {
                decryptWithKeystore(rawBytes)
            } else {
                rawBytes
            }
            val parts = String(plainBytes, Charsets.UTF_8).trim().split(":")
            if (parts.size < 2) return null
            val saltHex = parts[0]
            val hashHex = parts[1]
            if (saltHex.isEmpty() || hashHex.isEmpty()) null else (saltHex to hashHex)
        } catch (e: Exception) {
            null
        }
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    private fun encryptWithKeystore(plainBytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        val ivBase64 = Base64.getEncoder().encodeToString(cipher.iv)
        val cipherBase64 = Base64.getEncoder().encodeToString(cipher.doFinal(plainBytes))
        return "$ivBase64:$cipherBase64".toByteArray(Charsets.UTF_8)
    }

    private fun decryptWithKeystore(wrappedBytes: ByteArray): ByteArray {
        val parts = String(wrappedBytes, Charsets.UTF_8).trim().split(":")
        require(parts.size == 2) { "Dữ liệu mã hóa không hợp lệ" }
        val iv = Base64.getDecoder().decode(parts[0])
        val cipherBytes = Base64.getDecoder().decode(parts[1])
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), GCMParameterSpec(GCM_TAG_BITS, iv))
        return cipher.doFinal(cipherBytes)
    }

    private fun hexToBytes(hex: String): ByteArray {
        val clean = hex.trim()
        val result = ByteArray(clean.length / 2)
        for (i in result.indices) {
            val index = i * 2
            result[i] = clean.substring(index, index + 2).toInt(16).toByte()
        }
        return result
    }
}
