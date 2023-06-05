package com.android.mediproject.core.domain.sign

import com.android.mediproject.core.data.remote.sign.SignRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyUserInfoUseCase @Inject constructor(private val signRepository: SignRepository) {
    suspend operator fun invoke(): Flow<Long> = signRepository.myId
}