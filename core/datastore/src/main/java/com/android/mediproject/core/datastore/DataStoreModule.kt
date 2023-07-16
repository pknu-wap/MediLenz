package com.android.mediproject.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
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
        serializer: SavedTokenSerializer,
    ): DataStore<SavedToken> = DataStoreFactory.create(
        serializer = serializer,
        scope = CoroutineScope(ioDispatcher + SupervisorJob()),
    ) {
        context.dataStoreFile("saved_token.pb")
    }

    @Provides
    @Singleton
    fun providesAppDataStore(
        @ApplicationContext context: Context,
    ): AppDataStore = AppDataStoreImpl(context)
}
