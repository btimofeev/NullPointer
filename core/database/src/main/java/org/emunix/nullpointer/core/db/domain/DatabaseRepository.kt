package org.emunix.nullpointer.core.db.domain

import kotlinx.coroutines.flow.Flow
import org.emunix.nullpointer.core.common.UploadedFileModel
import java.util.Date

interface DatabaseRepository {

    suspend fun addToHistory(
        url: String,
        name: String,
        size: Long,
        uploadDate: Date,
    )

    suspend fun deleteFromHistory(url: String)

    suspend fun clearHistory()

    suspend fun getHistory(): Flow<List<UploadedFileModel>>
}