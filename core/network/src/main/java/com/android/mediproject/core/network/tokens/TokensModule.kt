package com.android.mediproject.core.network.tokens

import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.model.token.TokenState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokensModule {

    @Provides
    @Singleton
    fun providesTokenServer(
        aesCoder: AesCoder,
    ): TokenServer = TokenServerImpl(aesCoder, TokenState.Empty)
}
