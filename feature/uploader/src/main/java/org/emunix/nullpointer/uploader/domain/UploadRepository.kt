package org.emunix.nullpointer.uploader.domain

import java.io.File

interface UploadRepository {

    suspend fun upload(
        file: File,
    ): String
}