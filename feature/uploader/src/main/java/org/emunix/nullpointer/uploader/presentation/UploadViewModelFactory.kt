package org.emunix.nullpointer.uploader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.uploader.domain.UploadRepository

class UploadViewModelFactory(
    private val repository: UploadRepository,
    private val history: DatabaseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = UploadViewModel(repository, history) as T
}