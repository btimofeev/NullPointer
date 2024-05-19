package org.emunix.nullpointer.uploader.api.domain

import org.emunix.nullpointer.core.common.UploadedFileModel
import java.io.InputStream

interface UploadRepository {

    suspend fun upload(
        fileName: String,
        stream: InputStream,
    ): Result<UploadedFileModel>
}