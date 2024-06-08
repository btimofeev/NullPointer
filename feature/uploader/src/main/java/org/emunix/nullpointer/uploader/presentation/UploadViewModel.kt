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
import org.emunix.nullpointer.uploader.presentation.model.ScreenState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.ChooseFileState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadFailure
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadInProgressState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadSuccess
import org.emunix.nullpointer.uploader.work.UploadWorkManager

internal class UploadViewModel(
    private val uploadWorkManager: UploadWorkManager,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenState>(ChooseFileState)
    private var lastSelectedFileName: String = ""
    private var lastSelectedUri: String = ""

    val screenState: StateFlow<ScreenState> = _state.asStateFlow()

    init {
        observeUploadWork()
    }

    fun uploadFile(fileName: String, uri: String) {
        lastSelectedFileName = fileName
        lastSelectedUri = uri
        uploadWorkManager.startUpload(fileName, uri)
    }

    fun tryAgain() {
        uploadFile(lastSelectedFileName, lastSelectedUri)
    }

    private fun observeUploadWork() = viewModelScope.launch {
        uploadWorkManager.observeUpload().collect { status ->
            when (status) {
                is Running -> setScreenState(UploadInProgressState)
                is Success -> setScreenState(UploadSuccess(status.url))
                is Failed -> setScreenState(UploadFailure)
                is Cancelled -> setScreenState(ChooseFileState)
                is Unavailable -> Unit
            }
        }
    }

    private fun setScreenState(state: ScreenState) {
        _state.value = state
    }
}