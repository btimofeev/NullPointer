package org.emunix.nullpointer.uploader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.core.api.domain.DatabaseRepository
import org.emunix.nullpointer.uploader.domain.UploadRepository
import org.emunix.nullpointer.uploader.work.UploadWorkManager

internal class UploadViewModelFactory(
    private val uploadWorkManager: UploadWorkManager,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = UploadViewModel(uploadWorkManager) as T
}