package org.emunix.nullpointer.domain

import org.emunix.nullpointer.domain.model.UploadedFileModel
import java.io.InputStream

interface UploadRepository {

    suspend fun upload(
        fileName: String,
        stream: InputStream,
    ): Result<UploadedFileModel>
}