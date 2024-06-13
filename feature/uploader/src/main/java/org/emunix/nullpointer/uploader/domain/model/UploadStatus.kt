package org.emunix.nullpointer.uploader.domain.model

internal sealed interface UploadStatus {

    data class Success(val url: String): UploadStatus

    data object Running: UploadStatus

    data class Failed(val errorType: ErrorType): UploadStatus

    data object Cancelled: UploadStatus

    data object Unavailable: UploadStatus
}