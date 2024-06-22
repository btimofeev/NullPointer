package org.emunix.nullpointer.uploader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.domain.ShareAction.COPY_URL_TO_CLIPBOARD
import org.emunix.nullpointer.core.api.domain.ShareAction.NONE
import org.emunix.nullpointer.core.api.domain.ShareAction.SHARE_URL
import org.emunix.nullpointer.core.api.domain.UploadStatus.Cancelled
import org.emunix.nullpointer.core.api.domain.UploadStatus.Failed
import org.emunix.nullpointer.core.api.domain.UploadStatus.Running
import org.emunix.nullpointer.core.api.domain.UploadStatus.Success
import org.emunix.nullpointer.core.api.domain.UploadStatus.Unavailable
import org.emunix.nullpointer.core.api.domain.UploadWorkManager
import org.emunix.nullpointer.uikit.model.Action
import org.emunix.nullpointer.uikit.model.Action.CopyLink
import org.emunix.nullpointer.uikit.model.Action.ShareLink
import org.emunix.nullpointer.uploader.presentation.model.ScreenState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.ChooseFileState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.Error
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadInProgressState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadSuccess

internal class UploadViewModel(
    private val uploadWorkManager: UploadWorkManager,
    private val preferencesProvider: PreferencesProvider,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenState>(ChooseFileState)
    private val _command = Channel<Action>()
    private var lastSelectedFileName: String = ""
    private var lastSelectedUri: String = ""

    val screenState: StateFlow<ScreenState> = _state.asStateFlow()
    val command = _command.receiveAsFlow()

    init {
        observeUploadWork()
    }

    fun uploadFile(fileName: String, uri: String) {
        lastSelectedFileName = fileName
        lastSelectedUri = uri
        uploadWorkManager.startUpload(fileName, uri)
    }

    fun cancelUpload() {
        uploadWorkManager.cancelUpload()
    }

    fun tryAgain() {
        uploadFile(lastSelectedFileName, lastSelectedUri)
    }

    private fun observeUploadWork() = viewModelScope.launch {
        uploadWorkManager.observeUpload().collect { status ->
            when (status) {
                is Running -> setScreenState(UploadInProgressState)
                is Success -> setScreenState(UploadSuccess(status.url)).also { performAutoAction(status.url) }
                is Failed -> setScreenState(Error(status.errorType))
                is Cancelled -> setScreenState(ChooseFileState)
                is Unavailable -> Unit
            }
        }
    }

    private fun setScreenState(state: ScreenState) {
        _state.value = state
    }

    private fun performAutoAction(url: String) {
        when (preferencesProvider.actionAfterUpload) {
            SHARE_URL -> _command.trySend(ShareLink(url))
            COPY_URL_TO_CLIPBOARD -> _command.trySend(CopyLink(url))
            NONE -> Unit
        }
    }
}