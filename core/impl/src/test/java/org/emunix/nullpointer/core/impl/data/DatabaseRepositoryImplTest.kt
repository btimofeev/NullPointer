package org.emunix.nullpointer.core.impl.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.emunix.nullpointer.core.api.data.HistoryDao
import org.emunix.nullpointer.core.api.data.UploadedFileDbModel
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

class DatabaseRepositoryImplTest {

    private lateinit var historyDao: HistoryDao
    private lateinit var databaseRepository: DatabaseRepository

    @Before
    fun setUp() {
        historyDao = mockk(relaxed = true)
        databaseRepository = DatabaseRepositoryImpl(historyDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `check addToHistory`() = runTest {
        val expectedModel = UploadedFileDbModel(
            url = "test_url",
            name = "test_name",
            size = 100,
            uploadDate = Date(2500),
            token = "123",
        )

        databaseRepository.addToHistory(
            url = "test_url",
            name = "test_name",
            size = 100,
            uploadDate = Date(2500),
            token = "123",
        )

        coVerify { historyDao.insertWithOldToken(expectedModel) }
    }

    @Test
    fun `check deleteFromHistory`() = runTest {
        databaseRepository.deleteFromHistory("test_url")

        coVerify { historyDao.delete("test_url") }
    }

    @Test
    fun `check clearHistory`() = runTest {
        databaseRepository.clearHistory()

        coVerify { historyDao.deleteAll() }
    }

    @Test
    fun `check getHistory`() = runTest {
        val expectedList = listOf(
            UploadedFileModel(
                url = "test_url",
                name = "test_name",
                size = 100,
                uploadDate = Date(2500),
                token = "123",
            ),
            UploadedFileModel(
                url = "test_url 2",
                name = "test_name 2",
                size = 200,
                uploadDate = Date(5500),
                token = "123",
            )
        )
        coEvery { historyDao.observeAll() } returns flowOf(
            listOf(
                UploadedFileDbModel(
                    url = "test_url",
                    name = "test_name",
                    size = 100,
                    uploadDate = Date(2500),
                    token = "123",
                ),
                UploadedFileDbModel(
                    url = "test_url 2",
                    name = "test_name 2",
                    size = 200,
                    uploadDate = Date(5500),
                    token = "123",
                )
            )
        )

        val items = databaseRepository.getHistory().first()

        assert(items == expectedList)
    }
}