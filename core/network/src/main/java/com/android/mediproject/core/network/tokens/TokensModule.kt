package com.android.mediproject.core.network.tokens

import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import com.android.mediproject.core.datastore.SavedToken
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
        tokenDataStore: DataStore<SavedToken>,
        aesCoder: AesCoder,
    ): TokenServer = TokenServerImpl(tokenDataStore, aesCoder)
}
