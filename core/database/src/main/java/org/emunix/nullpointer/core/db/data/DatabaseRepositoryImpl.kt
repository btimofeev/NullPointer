package org.emunix.nullpointer.core.db.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.emunix.nullpointer.core.common.UploadedFileModel
import org.emunix.nullpointer.core.db.domain.DatabaseRepository
import java.util.Date
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : DatabaseRepository {

    override suspend fun addToHistory(url: String, name: String, size: Long, uploadDate: Date) {
        historyDao.insert(
            UploadedFileDbModel(url, name, size, uploadDate)
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
                    )
                }
            }
    }
}