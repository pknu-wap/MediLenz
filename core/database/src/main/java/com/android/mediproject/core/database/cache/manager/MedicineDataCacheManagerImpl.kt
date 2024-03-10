package com.android.mediproject.core.database.cache.manager

import com.android.mediproject.core.database.cache.MedicineDetailCacheDao
import com.android.mediproject.core.database.cache.MedicineDetailCacheDto
import com.android.mediproject.core.database.cache.MedicineImageCacheDao
import com.android.mediproject.core.database.cache.MedicineImageCacheDto
import com.android.mediproject.core.database.zip.Zipper
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)

class MedicineDataCacheManagerImpl @Inject constructor(
    private val medicineDetailCacheDao: MedicineDetailCacheDao,
    private val medicineImageCacheDao: MedicineImageCacheDao,
    private val zipper: Zipper,
) : MedicineDataCacheManager(medicineDetailCacheDao) {

    private val cacheChannel = Channel<CacheType>(capacity = Channel.UNLIMITED)

    private val job = GlobalScope.launch(Dispatchers.Default) {
        cacheChannel.consumeAsFlow().collect { cacheType ->
            // if (!checkNeedsUpdate(cacheType.entity.itemSequence, cacheType.entity.changeDate)) return@collect

            when (cacheType) {
                is CacheType.Image -> {
                    medicineImageCacheDao.update(
                        MedicineImageCacheDto(
                            itemSeq = cacheType.entity.itemSequence,
                            imageUrl = cacheType.entity.imageUrl,
                        ),
                    )
                }

                is CacheType.Detail -> {
                    medicineDetailCacheDao.update(
                        MedicineDetailCacheDto(
                            itemSeq = cacheType.entity.itemSequence,
                            data = zipper.compress(cacheType.entity.json),
                            changeDate = cacheType.entity.changeDate,
                        ),
                    )
                }
            }
        }
    }

    override suspend fun selectWithImageUrls(ids: List<String>) = medicineDetailCacheDao.selectWithImages(ids).map {
        MedicineCacheEntity(
            itemSequence = it.itemSeq,
            json = decompress(it.data),
            imageUrl = it.imageUrl,
        )
    }

    override suspend fun select(ids: List<String>) = medicineDetailCacheDao.select(ids).map {
        MedicineCacheEntity(
            itemSequence = it.itemSeq,
            json = decompress(it.data),
        )
    }

    override fun updateDetail(entity: MedicineCacheEntity) {
        cacheChannel.trySend(CacheType.Detail(entity))
    }

    override fun updateImage(entity: MedicineCacheEntity) {
        cacheChannel.trySend(CacheType.Image(entity))
    }

    override suspend fun select(id: String) = medicineDetailCacheDao.select(id).run {
        MedicineCacheEntity(
            itemSequence = itemSeq,
            json = decompress(data),
        )
    }

    private suspend fun checkNeedsUpdate(id: String, date: String): Boolean = medicineDetailCacheDao.selectChangeDate(id) != date

    private suspend fun decompress(src: ByteArray): String = zipper.decompress(src)

    sealed class CacheType(val entity: MedicineCacheEntity) {
        class Detail(entity: MedicineCacheEntity) : CacheType(entity)
        class Image(entity: MedicineCacheEntity) : CacheType(entity)
    }
}
