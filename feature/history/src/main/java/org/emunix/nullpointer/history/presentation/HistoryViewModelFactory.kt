package org.emunix.nullpointer.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.core.api.domain.PreferencesProvider

internal class HistoryViewModelFactory(
    private val history: DatabaseRepository,
    private val preferencesProvider: PreferencesProvider,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HistoryViewModel(history, preferencesProvider) as T
}