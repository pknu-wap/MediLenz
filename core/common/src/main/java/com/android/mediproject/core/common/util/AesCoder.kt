package com.android.mediproject.core.common.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
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
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        WeakReference(packageInfo.firstInstallTime.hashCode().toString().substring(0, 32)).apply {
            get()?.also {
                secretKey = SecretKeySpec(it.toByteArray(charset("UTF-8")), "AES")
                ivParameterSpec = IvParameterSpec(it.encodeToByteArray(0, 16))
            }
            clear()
        }
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
        return Base64.encode(encrpytionByte, 0, encrpytionByte.size)
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
        return cipher.doFinal(decodeByte).map { it.toInt().toChar() }.toCharArray()
    }
}