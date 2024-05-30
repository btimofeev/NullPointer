package org.emunix.nullpointer.core.db.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: UploadedFileDbModel)

    @Query("DELETE FROM history WHERE url = :url")
    suspend fun delete(url: String)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history")
    fun observeAll(): Flow<List<UploadedFileDbModel>>
}
