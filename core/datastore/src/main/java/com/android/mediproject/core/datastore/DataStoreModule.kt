package com.android.mediproject.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import com.android.mediproject.core.common.util.AesCoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
    @Provides
    @Singleton
    fun providesTokenDataStore(
    @ApplicationContext context: Context,
    @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    serializer: ConnectionTokenSerializer,
    ): DataStore<Any> = DataStoreFactory.create(
    serializer = serializer,
    scope = CoroutineScope(ioDispatcher + SupervisorJob()),
    ) {
    context.dataStoreFile("saved_token.pb")
    }
     */

    @Provides
    @Singleton
    fun providesTokenDataSource(
        tokenDataStore: DataStore<SavedToken>,
        aesCoder: AesCoder,
        tokenServer: TokenServer,
    ): TokenDataSource = TokenDataSourceImpl(tokenDataStore, aesCoder, tokenServer)

    @Provides
    @Singleton
    fun providesTokenServer(): TokenServer = TokenServerImpl()

    @Provides
    @Singleton
    fun providesAppDataStore(
        @ApplicationContext context: Context,
    ): AppDataStore = AppDataStoreImpl(context)
}