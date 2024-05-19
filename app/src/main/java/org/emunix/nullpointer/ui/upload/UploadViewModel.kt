package org.emunix.nullpointer.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.uploader.api.domain.UploadRepository
import org.emunix.nullpointer.core.common.UploadedFileModel
import java.io.InputStream

class UploadViewModel(
    private val repository: UploadRepository,
) : ViewModel() {

    private val _url = MutableStateFlow<String?>(null)

    val url: StateFlow<String?> = _url.asStateFlow()

    fun uploadFile(
        fileName: String,
        stream: InputStream,
    ) {
        viewModelScope.launch {
            repository.upload(fileName, stream)
                .onSuccess { showUrl(it) }
                .onFailure { _url.value = "Не удалось загрузить файл" }
        }
    }

    private fun showUrl(model: UploadedFileModel) {
        _url.value = model.url
    }
}