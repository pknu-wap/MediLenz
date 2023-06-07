package com.android.mediproject.core.common.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@Singleton
class AesCoder @Inject constructor(@ApplicationContext context: Context) {
    private var secretKey: SecretKeySpec? = null
    private var ivParameterSpec: IvParameterSpec? = null
    private val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

    init {
        val packageInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) context.packageManager.getPackageInfo(context.packageName,
                PackageManager.PackageInfoFlags.of(0))
            else context.packageManager.getPackageInfo(context.packageName, 0)

        var key: String? = "${packageInfo.firstInstallTime}".repeat(4).substring(0, 32)
        key?.apply {
            secretKey = SecretKeySpec(toByteArray(charset("UTF-8")), "AES")
            ivParameterSpec = IvParameterSpec(encodeToByteArray(0, 16))
        }
        key = null
    }

    private fun createSecretKey(plainArray: CharArray): Pair<SecretKeySpec, IvParameterSpec> {
        val keyLength = plainArray.size
        val extraLength = 32 - keyLength

        val key = ByteArray(32).apply {
            plainArray.mapIndexed { index, c ->
                this[index] = c.code.toByte()
            }
            if (extraLength > 0) {
                val extra = "5".toByte()
                IntRange(0, extraLength).forEachIndexed { index, i ->
                    this[keyLength + index] = extra
                }
            }
        }
        key.fill(0)
        return (SecretKeySpec(key, "AES") to IvParameterSpec(key.copyOfRange(0, 16)))
    }

    /**
     * 비밀번호 암호화
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun encodePassword(email: CharArray, password: CharArray): String {
        val (pwSecretKey, pwIvParameterSpec) = createSecretKey(email)
        cipher.init(Cipher.ENCRYPT_MODE, pwSecretKey, pwIvParameterSpec)
        val encrpytionByte = cipher.doFinal(ByteArray(password.size) { password[it].code.toByte() })
        val result = Base64.encode(encrpytionByte, 0, encrpytionByte.size)
        encrpytionByte.fill(0)
        return result
    }

    /**
     * 암호화
     *
     * @param plainArray 암호화할 문자 배열
     * @return 암호화된 문자열
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun encode(plainArray: CharArray): String {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        val encrpytionByte = cipher.doFinal(ByteArray(plainArray.size) { plainArray[it].code.toByte() })
        val result = Base64.encode(encrpytionByte, 0, encrpytionByte.size)
        encrpytionByte.fill(0)
        return result
    }

    /**
     * 복호화
     *
     * @param encodedText 복호화할 문자열
     * @return 복호화된 문자 배열
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun decode(encodedText: String): CharArray {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        val decodeByte = Base64.decode(encodedText, 0, encodedText.length)
        val result = cipher.doFinal(decodeByte).map { it.toInt().toChar() }.toCharArray()
        decodeByte.fill(0)
        return result
    }
}