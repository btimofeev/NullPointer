package org.emunix.nullpointer.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository

internal class HistoryViewModelFactory(
    private val history: DatabaseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryViewModel(history) as T
}