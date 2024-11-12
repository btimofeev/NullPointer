package org.emunix.nullpointer.core.impl.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.emunix.nullpointer.core.api.data.HistoryDao
import org.emunix.nullpointer.core.api.data.UploadedFileDbModel
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import java.util.Date
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : DatabaseRepository {

    override suspend fun addToHistory(
        url: String,
        name: String,
        size: Long,
        uploadDate: Date,
        token: String,
    ) {
        historyDao.insertWithOldToken(
            UploadedFileDbModel(url, name, size, uploadDate, token)
        )
    }

    override suspend fun deleteFromHistory(url: String) {
        historyDao.delete(url)
    }

    override suspend fun clearHistory() {
        historyDao.deleteAll()
    }

    override suspend fun getHistory(): Flow<List<UploadedFileModel>> {
        return historyDao.observeAll()
            .map { entries ->
                entries.map { entry ->
                    UploadedFileModel(
                        name = entry.name,
                        url = entry.url,
                        size = entry.size,
                        uploadDate = entry.uploadDate,
                        token = entry.token,
                    )
                }
            }
    }
}