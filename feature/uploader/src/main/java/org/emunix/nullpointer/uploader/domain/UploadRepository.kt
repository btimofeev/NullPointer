package org.emunix.nullpointer.uploader.domain

import org.emunix.nullpointer.uploader.domain.model.UploadResponse
import java.io.File

interface UploadRepository {

    suspend fun upload(
        file: File,
    ): UploadResponse
}