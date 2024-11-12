package org.emunix.nullpointer.history.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.ShareAction
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import org.emunix.nullpointer.history.presentation.model.HistoryItem
import org.emunix.nullpointer.history.utils.getIconResForFile
import org.emunix.nullpointer.uikit.model.Action
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private lateinit var database: DatabaseRepository
    private lateinit var preferencesProvider: PreferencesProvider
    private lateinit var dateFormatter: DateFormat

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        database = mockk(relaxed = true)
        preferencesProvider = mockk(relaxed = true)
        dateFormatter = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `check isSwipeToDeleteEnabled with ENABLED state`() = runTest {
        every { preferencesProvider.swipeToDeleteHistoryItem } returns true
        coEvery { database.getHistory() } returns flowOf(emptyList())

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        viewModel.init()

        val isEnabled = viewModel.isSwipeToDeleteEnabled.first()
        assert(isEnabled)
    }

    @Test
    fun `check isSwipeToDeleteEnabled with DISABLED state`() = runTest {
        every { preferencesProvider.swipeToDeleteHistoryItem } returns false
        coEvery { database.getHistory() } returns flowOf(emptyList())

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        viewModel.init()

        val isEnabled = viewModel.isSwipeToDeleteEnabled.first()
        assert(!isEnabled)
    }

    @Test
    fun `check historyItems`() = runTest {
        val historyModel = UploadedFileModel(
            name = "1",
            size = 2,
            url = "3",
            uploadDate = Date(4),
            token = "123",
        )
        val expected = HistoryItem(fileName = "1", url = "3", uploadDate = "4", iconRes = 5)
        mockkStatic(::getIconResForFile)
        every { getIconResForFile(any()) } returns 5
        every { dateFormatter.format(any()) } returns "4"
        coEvery { database.getHistory() } returns flowOf(listOf(historyModel))

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        viewModel.init()

        advanceUntilIdle()
        val items = viewModel.historyItems.first()
        assert(items == listOf(expected))
    }

    @Test
    fun `check onClearHistoryClick`() = runTest {
        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        viewModel.onClearHistoryClick()

        advanceUntilIdle()
        coVerify { database.clearHistory() }
    }

    @Test
    fun `check onRemoveHistoryItem`() = runTest {
        val item = mockk<HistoryItem> {
            every { url } returns "test_url"
        }

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        viewModel.onRemoveHistoryItem(item)

        advanceUntilIdle()
        coVerify { database.deleteFromHistory("test_url") }
    }

    @Test
    fun `check onItemClick with SHARE_URL preference`() = runTest {
        every { preferencesProvider.actionOnHistoryItemClick } returns ShareAction.SHARE_URL
        val item = mockk<HistoryItem> {
            every { url } returns "test_url"
        }

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        var action: Action? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.command.collect { action = it }
        }
        viewModel.onItemClick(item)

        advanceUntilIdle()
        assert((action as Action.ShareLink).url == "test_url")
    }

    @Test
    fun `check onItemClick with COPY_URL_TO_CLIPBOARD preference`() = runTest {
        every { preferencesProvider.actionOnHistoryItemClick } returns ShareAction.COPY_URL_TO_CLIPBOARD
        val item = mockk<HistoryItem> {
            every { url } returns "test_url"
        }

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        var action: Action? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.command.collect { action = it }
        }
        viewModel.onItemClick(item)

        advanceUntilIdle()
        assert((action as Action.CopyLink).url == "test_url")
    }

    @Test
    fun `check onItemClick with NONE preference`() = runTest {
        every { preferencesProvider.actionOnHistoryItemClick } returns ShareAction.NONE
        val item = mockk<HistoryItem> {
            every { url } returns "test_url"
        }

        val viewModel = HistoryViewModel(database, preferencesProvider, dateFormatter)
        var action: Action? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.command.collect { action = it }
        }
        viewModel.onItemClick(item)

        advanceUntilIdle()
        assert(action == null)
    }
}