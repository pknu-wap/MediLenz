package com.android.mediproject.core.domain

import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.data.user.UserRepository
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.model.requestparameters.ChangeNicknameParameter
import com.android.mediproject.core.model.requestparameters.ChangePasswordParameter
import javax.inject.Inject


class EditUserAccountUseCase @Inject constructor(
    private val appDataStore: AppDataStore,
    private val userRepository: UserRepository,
    private val signRepository: SignRepository,
) {
    suspend fun changeNickname(changeNicknameParameter: ChangeNicknameParameter) = userRepository.changeNickname(changeNicknameParameter).fold(
        onSuccess = {
            appDataStore.saveNickName(changeNicknameParameter.newNickname)
            Result.success(it)
        },
        onFailure = { Result.failure(it) },
    )

    suspend fun changePassword(changePasswordParameter: ChangePasswordParameter) = userRepository.changePassword(
        changePasswordParameter,
    ).fold(
        onSuccess = {
            Result.success(it)
        },
        onFailure = { Result.failure(it) },
    )

    suspend fun withdrawal() {
        userRepository.withdrawal()
        signRepository.signOut()
        appDataStore.clearMyAccountInfo()
    }
    
}
