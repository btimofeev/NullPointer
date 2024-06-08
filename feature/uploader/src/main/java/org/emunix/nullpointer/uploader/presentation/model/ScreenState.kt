package org.emunix.nullpointer.uploader.presentation.model

internal interface ScreenState {

    data object ChooseFileState : ScreenState

    data object UploadInProgressState : ScreenState

    data class UploadSuccess(val url: String) : ScreenState

    data object UploadFailure : ScreenState
}