package org.emunix.nullpointer.uploader.presentation.model

import org.emunix.nullpointer.core.api.domain.ErrorType

internal interface ScreenState {

    data object ChooseFileState : ScreenState

    data object UploadInProgressState : ScreenState

    data class UploadSuccess(val url: String) : ScreenState

    data class Error(val type: ErrorType) : ScreenState
}