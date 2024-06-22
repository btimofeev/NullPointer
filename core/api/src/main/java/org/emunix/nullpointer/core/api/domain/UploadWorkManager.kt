package org.emunix.nullpointer.core.api.domain

import kotlinx.coroutines.flow.Flow

interface UploadWorkManager {

    fun startUpload(
        fileName: String,
        uri: String,
    )

    fun observeUpload(): Flow<UploadStatus>

    fun cancelUpload()
}