package com.jjim4963tw.library.utility

import android.os.Build
import android.util.Base64
import java.lang.Exception
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

object EncryptUtility {
    //加密模式 -> algorithm/mode/padding
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private const val CIPHER = "AES"
    private const val HASH_MD5 = "MD5"
    private const val HASH_1 = "SHA-1"
    private const val HASH_256 = "SHA-256"

    fun md5(input: String) = hashString(HASH_MD5, input)

    fun base64Encode(input: ByteArray): String = Base64.encodeToString(input, Base64.DEFAULT)

    fun base64Decode(input: String): ByteArray = Base64.decode(input, Base64.DEFAULT)

    fun base64DecodeToString(input: String): String = String(Base64.decode(input, Base64.DEFAULT))

    fun encryptByAES(key: String, input: String): String {
        val cipher = initAES(key, Cipher.ENCRYPT_MODE)
        val encrypt = cipher.doFinal(input.toByteArray())

        return base64Encode(encrypt)
    }

    fun decryptByAES(key: String, input: String): String {
        val cipher = initAES(key, Cipher.DECRYPT_MODE)

        val encrypt = try {
            cipher.doFinal(base64Decode(input))
        } catch (e: Exception) {
            byteArrayOf()
        }

        return String(encrypt)
    }

    private fun initAES(key: String, mode: Int): Cipher {
        val keyAes = genKey(key)
        val keySpec = SecretKeySpec(keyAes.toByteArray(), 0, 32, CIPHER)
        val ivParameterSpec = IvParameterSpec(keyAes.toByteArray(), 0, 16)

        return run {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(mode, keySpec, ivParameterSpec)

            cipher
        }
    }

    private fun genKey(input: String): String {
        val combineKey = StringBuilder("ASUS${input}Cloud${Build.BRAND}${Build.DEVICE}")
        if (combineKey.length < 32) {
            combineKey.append("ASUSCloudInc70538068")
        }
        val key = base64Encode(combineKey.toString().toByteArray())
        val p = 3
        return with(StringBuilder()) {
            key.trim().toCharArray().forEach {
                var ascii = it.toInt()
                ascii += p
                append(ascii.toChar())
            }
            toString()
        }
    }

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())

        return bytesToHex(bytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)

        for (j in bytes.indices) {
            val v = (bytes[j] and 0xFF.toByte()).toInt()

            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}