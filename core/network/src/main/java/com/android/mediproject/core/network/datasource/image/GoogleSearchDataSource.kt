package com.android.mediproject.core.network.datasource.image

interface GoogleSearchDataSource {
    suspend fun getImageUrl(medicineName: String): Result<String>
}
