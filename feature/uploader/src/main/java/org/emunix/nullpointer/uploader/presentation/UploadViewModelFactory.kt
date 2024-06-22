package org.emunix.nullpointer.uploader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.UploadWorkManager

internal class UploadViewModelFactory(
    private val uploadWorkManager: UploadWorkManager,
    private val preferencesProvider: PreferencesProvider,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UploadViewModel(uploadWorkManager, preferencesProvider) as T
}