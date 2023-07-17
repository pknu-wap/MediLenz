package com.android.mediproject.core.database.zip

import net.jpountz.lz4.LZ4Compressor
import net.jpountz.lz4.LZ4Factory
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Zipper @Inject constructor() {

    private val lz4Factory = LZ4Factory.fastestInstance()

    /**
     * 문자열을 압축하여 ByteArray로 반환
     */
    suspend fun compress(src: String): ByteArray {
        return WeakReference(src.toByteArray()).get()?.let { srcArr ->
            val compressor: LZ4Compressor = lz4Factory.fastCompressor()
            val maxCompressedLength = compressor.maxCompressedLength(srcArr.size)

            val compressed = ByteArray(maxCompressedLength)
            val compressedLength = compressor.compress(srcArr, 0, srcArr.size, compressed, 0, maxCompressedLength)
            compressed.copyOf(compressedLength)
        } ?: byteArrayOf()
    }

    /**
     * 압축된 ByteArray를 문자열로 반환
     */
    suspend fun decompress(compressed: ByteArray): String {
        return WeakReference(ByteArray(compressed.size * 4)).get()?.let { decomp ->
            val decompressor = lz4Factory.safeDecompressor()
            val decompressedLength = decompressor.decompress(compressed, decomp)
            String(decomp.copyOf(decompressedLength))
        } ?: ""
    }

}
