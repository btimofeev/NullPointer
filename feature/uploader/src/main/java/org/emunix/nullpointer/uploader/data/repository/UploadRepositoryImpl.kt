package org.emunix.nullpointer.uploader.data.repository

import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import org.emunix.nullpointer.core.api.domain.ResponseIsEmptyException
import org.emunix.nullpointer.core.api.domain.UploadFailedException
import org.emunix.nullpointer.uploader.data.api.UploadApi
import org.emunix.nullpointer.uploader.domain.UploadRepository
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadApi: UploadApi,
) : UploadRepository {

    override suspend fun upload(
        file: File,
    ): String {
        val response = sendFile(file)
        if (response.isSuccessful) {
            return response.body() ?: throw ResponseIsEmptyException()
        } else {
            throw UploadFailedException()
        }
    }

    private suspend fun sendFile(file: File): Response<String> {
        return uploadApi.upload(
            part = Part
                .createFormData(
                    "file",
                    file.name,
                    RequestBody.create(
                        null,
                        file
                    )
                )
        )
    }
}