package com.android.mediproject.core.data.cache.repository

import com.android.mediproject.core.database.cache.MedicineCacheDao
import com.android.mediproject.core.database.cache.MedicineCacheDto
import com.android.mediproject.core.database.zip.Zipper
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class MedicineDataCacheRepositoryImpl @Inject constructor(
    private val medicineCacheDao: MedicineCacheDao,
    private val zipper: Zipper,
) : MedicineDataCacheRepository(medicineCacheDao) {

    @OptIn(DelicateCoroutinesApi::class) private val scope = GlobalScope + Dispatchers.Default

    override suspend fun selectAll() = medicineCacheDao.selectAll()

    override suspend fun selectAll(ids: List<String>) = medicineCacheDao.selectAll(ids)

    override suspend fun selectAllImageUrls(ids: List<String>) = medicineCacheDao.selectAllImageUrls(ids)

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

    override suspend fun select(id: String): MedicineCacheDto = medicineCacheDao.select(id)
}
