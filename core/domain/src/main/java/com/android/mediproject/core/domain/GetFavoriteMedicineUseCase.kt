package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.favoritemedicine.FavoriteMedicineRepository
import com.android.mediproject.core.model.favoritemedicine.CheckFavoriteMedicineResponse
import com.android.mediproject.core.model.favoritemedicine.toFavoriteMedicineDto
import com.android.mediproject.core.model.favoritemedicine.toFavoriteMedicineMoreDto
import com.android.mediproject.core.model.requestparameters.AddFavoriteMedicineParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteMedicineUseCase @Inject constructor(private val favoriteMedicineRepository: FavoriteMedicineRepository) {

    suspend fun getFavoriteMedicineList() = channelFlow {
        favoriteMedicineRepository.getFavoriteMedicineList().map { result ->
            result.fold(
                onSuccess = { Result.success(it.map { it.toFavoriteMedicineDto() }) },
                onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    suspend fun getFavoriteMedicineMoreList() = channelFlow {
        favoriteMedicineRepository.getFavoriteMedicineList().map { result ->
            result.fold(
                onSuccess = { Result.success(it.map { it.toFavoriteMedicineMoreDto() }) },
                onFailure = { Result.failure(it) })
        }.collectLatest { trySend(it) }
    }

    fun favoriteMedicine(medicineId: Long, like: Boolean): Flow<Result<Unit>> = channelFlow {
        if (like) {
            favoriteMedicineRepository.addFavortieMedicine(AddFavoriteMedicineParameter(medicineId))
                .collect { addFavoriteMedicineResponseResult ->
                    val result =
                        addFavoriteMedicineResponseResult.fold(
                            onSuccess = { Result.success(Unit) },
                            onFailure = { Result.failure(it) })
                    trySend(result)
                }
        } else {
            favoriteMedicineRepository.deleteFavoriteMedicine(medicineId)
                .collect { deleteFavoriteMedicineResponseResult ->
                    val result =
                        deleteFavoriteMedicineResponseResult.fold(
                            onSuccess = { Result.success(Unit) },
                            onFailure = { Result.failure(it) })
                    trySend(result)
                }
        }
    }

    fun checkFavoriteMedicine(medicineId: Long): Flow<Result<CheckFavoriteMedicineResponse>> =
        favoriteMedicineRepository.checkFavoriteMedicine(medicineId)
}