package com.example.composepractice.di

import android.content.Context
import com.example.composepractice.data.room.AppDatabase
import com.example.composepractice.data.room.DatabaseBuilder
import com.example.composepractice.data.room.DatabaseHelper
import com.example.composepractice.data.room.DatabaseHelperImpl
import com.example.composepractice.data.room.dao.AccountDAO
import com.example.composepractice.data.room.dao.NotesDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return DatabaseBuilder.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideDatabaseHelper(
        appDatabase: AppDatabase
    ): DatabaseHelperImpl = DatabaseHelperImpl(appDatabase)

    @Provides
    @Singleton
    fun provideRoomHelper(databaseHelperImpl: DatabaseHelperImpl): DatabaseHelper =
        databaseHelperImpl

    @Provides
    fun provideUserDao(database: AppDatabase): AccountDAO {
        return database.accountDAO()
    }

    @Provides
    fun provideNotesDao(database: AppDatabase): NotesDAO {
        return database.notesDAO()
    }
}