package org.emunix.nullpointer.uploader.domain.model

internal sealed interface UploadStatus {

    data class Success(val url: String): UploadStatus

    data object Running: UploadStatus

    data object Failed: UploadStatus

    data object Cancelled: UploadStatus

    data object Unavailable: UploadStatus
}