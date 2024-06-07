package org.emunix.nullpointer.uploader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.uploader.domain.model.UploadStatus.Cancelled
import org.emunix.nullpointer.uploader.domain.model.UploadStatus.Failed
import org.emunix.nullpointer.uploader.domain.model.UploadStatus.Unavailable
import org.emunix.nullpointer.uploader.domain.model.UploadStatus.Running
import org.emunix.nullpointer.uploader.domain.model.UploadStatus.Success
import org.emunix.nullpointer.uploader.work.UploadWorkManager

internal class UploadViewModel(
    private val uploadWorkManager: UploadWorkManager,
) : ViewModel() {

    private val _url = MutableStateFlow<String?>(null)

    val url: StateFlow<String?> = _url.asStateFlow()

    init {
        observeUploadWork()
    }

    fun uploadFile(fileName: String, uri: String) {
        uploadWorkManager.startUpload(fileName, uri)
    }

    private fun observeUploadWork() = viewModelScope.launch {
        uploadWorkManager.observeUpload().collect { status ->
            when (status) {
                is Running -> println("work is running")
                is Success -> showUrl(status.url)
                is Failed -> println("work is failed")
                is Cancelled -> println("work is cancelled")
                is Unavailable -> Unit
            }
        }
    }

    private fun showUrl(url: String) {
        _url.value = url
    }
}