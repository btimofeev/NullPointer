package org.emunix.nullpointer.uploader.impl.data.repository

import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import org.emunix.nullpointer.uploader.impl.data.api.UploadApi
import org.emunix.nullpointer.uploader.api.domain.UploadRepository
import org.emunix.nullpointer.core.common.CreateTempFileException
import org.emunix.nullpointer.core.common.ResponseIsEmptyException
import org.emunix.nullpointer.core.common.UploadFailedException
import org.emunix.nullpointer.core.common.UploadedFileModel
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.Date

class UploadRepositoryImpl(
    private val uploadApi: UploadApi,
    private val tempDir: File,
) : UploadRepository {

    override suspend fun upload(
        fileName: String,
        stream: InputStream,
    ): Result<UploadedFileModel> {
        val tempFile = try {
            makeTempFile(fileName, stream)
        } catch (e: IOException) {
            return Result.failure(CreateTempFileException())
        }

        return try {
            val response = sendFile(tempFile)
            getUploadedFileModel(response, tempFile)
        } catch (e: Throwable) {
            Result.failure(e)
        } finally {
            tempFile.delete()
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

    private fun getUploadedFileModel(response: Response<String>, file: File): Result<UploadedFileModel> {
        if (response.isSuccessful) {
            val body = response.body() ?: return Result.failure(ResponseIsEmptyException())
            return Result.success(
                UploadedFileModel(
                    name = file.name,
                    size = file.length(),
                    url = body,
                    uploadDate = Date(),
                )
            )
        } else {
            return Result.failure(UploadFailedException())
        }
    }

    private fun makeTempFile(
        fileName: String,
        stream: InputStream,
    ): File {
        val file = File(tempDir, fileName)
        stream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
                output.flush()
            }
        }
        return file
    }
}