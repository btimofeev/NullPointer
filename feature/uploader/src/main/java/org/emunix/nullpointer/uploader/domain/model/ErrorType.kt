package org.emunix.nullpointer.uploader.domain.model

internal enum class ErrorType {

    UPLOAD_FAILED,

    MAX_FILE_SIZE_HAS_BEEN_EXCEEDED,

    FORBIDDEN_FILE_FORMAT,
}