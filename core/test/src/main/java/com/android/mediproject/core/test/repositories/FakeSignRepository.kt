package com.android.mediproject.core.test.repositories

import com.android.mediproject.core.data.sign.SignRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class FakeSignRepository : SignRepository {
    override fun login(loginParameter: com.android.mediproject.core.model.requestparameters.LoginParameter): Flow<Result<Unit>> = channelFlow {
        trySend(Result.success(Unit))
    }

    override fun signUp(signUpParameter: com.android.mediproject.core.model.requestparameters.SignUpParameter): Flow<Result<Unit>> = channelFlow {
        trySend(Result.success(Unit))
    }

    override fun signOut() {
    }
}
