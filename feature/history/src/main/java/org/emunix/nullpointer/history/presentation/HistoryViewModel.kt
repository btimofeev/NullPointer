package org.emunix.nullpointer.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.ShareAction.COPY_URL_TO_CLIPBOARD
import org.emunix.nullpointer.core.api.domain.ShareAction.NONE
import org.emunix.nullpointer.core.api.domain.ShareAction.SHARE_URL
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import org.emunix.nullpointer.history.presentation.model.HistoryItem
import org.emunix.nullpointer.history.utils.getIconResForFile
import org.emunix.nullpointer.uikit.model.Action
import org.emunix.nullpointer.uikit.model.Action.CopyLink
import org.emunix.nullpointer.uikit.model.Action.ShareLink
import java.text.DateFormat

internal class HistoryViewModel(
    private val history: DatabaseRepository,
    private val preferencesProvider: PreferencesProvider,
    private val dateFormatter: DateFormat,
) : ViewModel() {

    private val _historyItems = MutableStateFlow<List<HistoryItem>?>(null)
    private val _isSwipeToDeleteEnabled = MutableStateFlow(false)
    private val _command = Channel<Action>()

    val historyItems: StateFlow<List<HistoryItem>?> = _historyItems.asStateFlow()
    val isSwipeToDeleteEnabled = _isSwipeToDeleteEnabled.asStateFlow()
    val command = _command.receiveAsFlow()

    fun init() {
        _isSwipeToDeleteEnabled.value = preferencesProvider.swipeToDeleteHistoryItem
        observeHistory()
    }

    fun onItemClick(item: HistoryItem) {
        when (preferencesProvider.actionOnHistoryItemClick) {
            SHARE_URL -> _command.trySend(ShareLink(item.url))
            COPY_URL_TO_CLIPBOARD -> _command.trySend(CopyLink(item.url))
            NONE -> Unit
        }
    }

    fun onRemoveHistoryItem(item: HistoryItem) = viewModelScope.launch {
        history.deleteFromHistory(item.url)
    }

    fun onClearHistoryClick() = viewModelScope.launch {
        history.clearHistory()
    }

    private fun observeHistory() = viewModelScope.launch {
            history.getHistory()
                .collect { items ->
                    _historyItems.value = items
                        .sortedByDescending { it.uploadDate }
                        .toHistoryItems()
                }
        }

    private fun List<UploadedFileModel>.toHistoryItems() =
        this.map { item ->
            HistoryItem(
                fileName = item.name,
                url = item.url,
                uploadDate = dateFormatter.format(item.uploadDate),
                iconRes = getIconResForFile(item.name)
            )
        }
}