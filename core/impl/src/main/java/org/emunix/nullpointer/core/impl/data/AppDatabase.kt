package org.emunix.nullpointer.core.impl.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.emunix.nullpointer.core.api.data.HistoryDao
import org.emunix.nullpointer.core.api.data.UploadedFileDbModel

@Database(entities = [UploadedFileDbModel::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}
