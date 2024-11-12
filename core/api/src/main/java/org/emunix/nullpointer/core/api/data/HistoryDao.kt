package org.emunix.nullpointer.core.api.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(file: UploadedFileDbModel)

    /**
     * The server is not returning a token after we re-upload a file that already exists on the server.
     * As a result, we are attempting to retrieve the token from the old record
     * so that it will not be replaced with an empty string.
     */
    @Transaction
    suspend fun insertWithOldToken(file: UploadedFileDbModel) {
        val oldToken = get(file.url)?.token
        if (file.token.isBlank() && !oldToken.isNullOrBlank()) {
            insert(file.copy(token = oldToken))
        } else {
            insert(file)
        }
    }

    @Query("SELECT * FROM history WHERE url LIKE :url LIMIT 1")
    suspend fun get(url: String): UploadedFileDbModel?

    @Query("DELETE FROM history WHERE url = :url")
    suspend fun delete(url: String)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history")
    fun observeAll(): Flow<List<UploadedFileDbModel>>
}
