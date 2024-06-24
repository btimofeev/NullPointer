package org.emunix.nullpointer.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.emunix.nullpointer.core.api.domain.ErrorType
import org.emunix.nullpointer.core.api.domain.UploadStatus
import org.emunix.nullpointer.core.api.domain.UploadWorkManager
import javax.inject.Inject

class TestUploadWorkManager @Inject constructor() : UploadWorkManager {

    private val uploadFlow = MutableStateFlow<UploadStatus>(UploadStatus.Unavailable)

    override fun startUpload(fileName: String, uri: String) {
        when (uri) {
            "content://file_with_error_upload" -> {
                uploadFlow.value = UploadStatus.Failed(ErrorType.UPLOAD_FAILED)
            }

            "content://file_with_success_upload" -> {
                uploadFlow.value = UploadStatus.Success(url = "http://mock.srv/abc.txt")
            }
        }
    }

    override fun observeUpload(): Flow<UploadStatus> = uploadFlow

    override fun cancelUpload() {
        uploadFlow.value = UploadStatus.Cancelled
    }
}