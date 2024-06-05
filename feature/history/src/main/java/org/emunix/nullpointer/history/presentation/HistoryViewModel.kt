package org.emunix.nullpointer.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import org.emunix.nullpointer.history.presentation.model.HistoryItem
import java.text.SimpleDateFormat

internal class HistoryViewModel(
    private val history: DatabaseRepository,
) : ViewModel() {

    private val _historyItems = MutableStateFlow<List<HistoryItem>>(emptyList())
    private val dateFormatter by lazy { SimpleDateFormat.getDateTimeInstance() }

    val historyItems: StateFlow<List<HistoryItem>> = _historyItems.asStateFlow()

    fun init() {
        observeHistory()
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
            )
        }
}