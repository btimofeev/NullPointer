package org.emunix.nullpointer.core.api.domain

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DatabaseRepository {

    suspend fun addToHistory(
        url: String,
        name: String,
        size: Long,
        uploadDate: Date,
        token: String,
    )

    suspend fun deleteFromHistory(url: String)

    suspend fun clearHistory()

    suspend fun getHistory(): Flow<List<UploadedFileModel>>
}