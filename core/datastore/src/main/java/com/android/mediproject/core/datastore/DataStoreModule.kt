package com.android.mediproject.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.util.AesCoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesTokenDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        serializer: ConnectionTokenSerializer,
    ): DataStore<ConnectionToken> = DataStoreFactory.create(
        serializer = serializer,
        scope = CoroutineScope(ioDispatcher + SupervisorJob()),
    ) {
        context.dataStoreFile("connection_token.pb")
    }

    @Provides
    @Singleton
    fun providesTokenDataSource(
        tokenDataStore: DataStore<ConnectionToken>,
        aesCoder: AesCoder,
        tokenServer: TokenServer,
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): TokenDataSource = TokenDataSourceImpl(tokenDataStore, aesCoder, tokenServer, ioDispatcher)

    @Provides
    @Singleton
    fun providesTokenServer(): TokenServer = TokenServerImpl()

    @Provides
    @Singleton
    fun providesAppDataStore(
        @ApplicationContext context: Context,
    ): AppDataStore = AppDataStoreImpl(context)
}