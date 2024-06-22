package org.emunix.nullpointer.core.api.domain

sealed interface UploadStatus {

    data class Success(val url: String): UploadStatus

    data object Running: UploadStatus

    data class Failed(val errorType: ErrorType): UploadStatus

    data object Cancelled: UploadStatus

    data object Unavailable: UploadStatus
}