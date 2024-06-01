package org.emunix.nullpointer.core.impl.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.api.data.HistoryDao
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.impl.data.AppDatabase
import org.emunix.nullpointer.core.impl.data.DatabaseRepositoryImpl
import javax.inject.Singleton

@Module
interface DatabaseModule {

    @Binds
    fun bindDatabaseRepository(impl: DatabaseRepositoryImpl): DatabaseRepository

    companion object {

        @Provides
        @Singleton
        internal fun provideDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()

        @Provides
        internal fun provideGameDAO(db: AppDatabase): HistoryDao = db.historyDao()
    }
}