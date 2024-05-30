package org.emunix.nullpointer.core.db.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.emunix.nullpointer.core.common.di.ApplicationContext
import org.emunix.nullpointer.core.db.data.AppDatabase
import org.emunix.nullpointer.core.db.data.DatabaseRepositoryImpl
import org.emunix.nullpointer.core.db.data.HistoryDao
import org.emunix.nullpointer.core.db.domain.DatabaseRepository
import javax.inject.Singleton

@Module
interface DatabaseModule {

    @Binds
    @Singleton
    fun bindDatabaseRepository(impl: DatabaseRepositoryImpl): DatabaseRepository

    companion object {

        @Provides
        @Singleton
        internal fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()

        @Provides
        @Singleton
        internal fun provideGameDAO(db: AppDatabase): HistoryDao = db.historyDao()
    }
}