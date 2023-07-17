package com.android.mediproject.core.data.cache.repository

import com.android.mediproject.core.database.cache.MedicineCacheDao
import com.android.mediproject.core.database.cache.MedicineCacheDto
import com.android.mediproject.core.database.zip.Zipper
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity
import javax.inject.Inject

class MedicineDataCacheRepositoryImpl @Inject constructor(
    private val medicineCacheDao: MedicineCacheDao,
    private val zipper: Zipper,
) : MedicineDataCacheRepository(medicineCacheDao) {

    override suspend fun selectAll() = medicineCacheDao.selectAll().map {
        MedicineCacheEntity(
            itemSequence = it.itemSeq,
            json = decompress(it),
            imageUrl = it.imageUrl,
        )
    }

    override suspend fun selectAll(ids: List<String>) = medicineCacheDao.selectAll(ids).map {
        MedicineCacheEntity(
            itemSequence = it.itemSeq,
            json = decompress(it),
            imageUrl = it.imageUrl,
        )
    }

    override suspend fun selectAllImageUrls(ids: List<String>) = medicineCacheDao.selectAllImageUrls(ids).map {
        MedicineCacheEntity(
            itemSequence = it.itemSeq,
            json = decompress(it),
            imageUrl = it.imageUrl,
        )
    }

    override suspend fun updates(entities: List<MedicineCacheEntity>) {
        val list = entities.map {
            MedicineCacheDto(
                itemSeq = it.itemSequence,
                data = zipper.compress(it.json),
                imageUrl = it.imageUrl,
            )
        }
        medicineCacheDao.update(list)
    }

    override suspend fun select(id: String) = medicineCacheDao.select(id).run {
        MedicineCacheEntity(
            itemSequence = itemSeq,
            json = decompress(this),
            imageUrl = imageUrl,
        )
    }

    private suspend fun decompress(dto: MedicineCacheDto): String = zipper.decompress(dto.data)
}
