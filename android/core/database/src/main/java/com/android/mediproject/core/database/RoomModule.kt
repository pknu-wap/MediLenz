package com.android.mediproject.core.database

import android.content.Context
import androidx.room.Room
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): RoomDB = Room.databaseBuilder(
        context, RoomDB::class.java, "medi_database"
    ).build()
}

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideSearchHistoryDao(roomDB: RoomDB): SearchHistoryDao = roomDB.searchHistoryDao()
}