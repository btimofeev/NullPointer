package org.emunix.nullpointer.uploader.presentation.model

import org.emunix.nullpointer.uploader.domain.model.ErrorType

internal interface ScreenState {

    data object ChooseFileState : ScreenState

    data object UploadInProgressState : ScreenState

    data class UploadSuccess(val url: String) : ScreenState

    data class Error(val type: ErrorType) : ScreenState
}