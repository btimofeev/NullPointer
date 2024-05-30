package org.emunix.nullpointer.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.uploader.api.domain.UploadRepository
import org.emunix.nullpointer.core.common.UploadedFileModel
import org.emunix.nullpointer.core.db.domain.DatabaseRepository
import java.io.InputStream

class UploadViewModel(
    private val repository: UploadRepository,
    private val history: DatabaseRepository,
) : ViewModel() {

    private val _url = MutableStateFlow<String?>(null)

    val url: StateFlow<String?> = _url.asStateFlow()

    fun uploadFile(
        fileName: String,
        stream: InputStream,
    ) {
        viewModelScope.launch {
            repository.upload(fileName, stream)
                .onSuccess {
                    showUrl(it)
                    addToHistory(it)
                }
                .onFailure { _url.value = "Не удалось загрузить файл" }
        }
    }

    private fun showUrl(model: UploadedFileModel) {
        _url.value = model.url
    }

    private suspend fun addToHistory(model: UploadedFileModel) {
        history.addToHistory(model.url, model.name, model.size, model.uploadDate)
    }
}