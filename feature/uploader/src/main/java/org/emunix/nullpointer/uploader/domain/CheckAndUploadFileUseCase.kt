package org.emunix.nullpointer.uploader.domain

import org.emunix.nullpointer.core.api.domain.CreateTempFileException
import org.emunix.nullpointer.core.api.domain.FileTypeIsForbiddenException
import org.emunix.nullpointer.core.api.domain.MaxFileSizeHasBeenExceedsException
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.util.Date
import javax.inject.Inject

interface CheckAndUploadFileUseCase {

    suspend operator fun invoke(
        fileName: String,
        stream: InputStream,
    ): Result<UploadedFileModel>
}

internal class CheckAndUploadFileUseCaseImpl @Inject constructor(
    private val repository: UploadRepository,
    private val tempDir: File,
) : CheckAndUploadFileUseCase {

    private val forbiddenMimeTypes = setOf(
        "application/x-dosexec",
        "application/x-executable",
        "application/x-sharedlib",
        "application/x-hdf5",
        "application/java-archive",
        "application/vnd.android.package-archive",
        "application/x-rar",
        "application/rar",
        "application/vnd.rar",
        "application/vnd.microsoft.portable-executable",
    )

    override suspend fun invoke(fileName: String, stream: InputStream): Result<UploadedFileModel> {
        val tempFile = try {
            makeTempFile(fileName, stream)
        } catch (e: IOException) {
            return Result.failure(CreateTempFileException())
        }

        try {
            if (isFileFormatForbidden(tempFile)) {
                return Result.failure(FileTypeIsForbiddenException())
            }
            if (tempFile.length() > SIZE_512_MB) {
                return Result.failure(MaxFileSizeHasBeenExceedsException())
            }

            val response = repository.upload(tempFile)
            return Result.success(getUploadedFileModel(response.url, response.token, tempFile))
        } catch (e: Exception) {
            return Result.failure(e)
        } finally {
            tempFile.delete()
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

    private fun getUploadedFileModel(url: String, token: String, file: File): UploadedFileModel =
        UploadedFileModel(
            name = file.name,
            size = file.length(),
            url = url,
            uploadDate = Date(),
            token = token,
        )

    private fun isFileFormatForbidden(file: File): Boolean {
        val fileMimeType = Files.probeContentType(file.toPath())
        return fileMimeType in forbiddenMimeTypes
    }

    companion object {

        const val SIZE_512_MB = 536870912
    }
}