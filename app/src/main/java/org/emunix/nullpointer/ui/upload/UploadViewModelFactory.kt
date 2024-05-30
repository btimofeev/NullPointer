package org.emunix.nullpointer.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.db.domain.DatabaseRepository
import org.emunix.nullpointer.uploader.api.domain.UploadRepository

class UploadViewModelFactory(
    private val repository: UploadRepository,
    private val history: DatabaseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = UploadViewModel(repository, history) as T
}