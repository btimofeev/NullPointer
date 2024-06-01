package org.emunix.nullpointer.uploader.api.domain

import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import java.io.InputStream

interface UploadRepository {

    suspend fun upload(
        fileName: String,
        stream: InputStream,
    ): Result<UploadedFileModel>
}