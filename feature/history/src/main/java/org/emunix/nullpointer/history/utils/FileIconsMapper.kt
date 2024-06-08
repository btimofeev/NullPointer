package org.emunix.nullpointer.history.utils

import java.io.File

internal val images = setOf("jpg", "jpeg", "png", "gif", "bmp", "psd", "svg")

internal val video = setOf("mp4", "mpg", "mpeg", "mov", "wmv", "avi", "mkv", "ogv", "3gp")

internal val audio = setOf("mp3", "ogg", "oga", "wav", "flac", "aiff", "aac", "opus", "mod", "xm", "it", "s3m")

internal val archives = setOf("zip", "rar", "tar", "gz", "bz2", "7z", "aar")

internal val books = setOf("fb2", "mobi", "epub", "cbr", "cbz", "cb7", "opf")

internal val pdf = setOf("pdf")

internal fun getIconResForFile(fileName: String) : Int {
    val ext = File(fileName).extension
    return when (ext) {
        in images -> org.emunix.nullpointer.uikit.R.drawable.ic_image_24dp
        in video -> org.emunix.nullpointer.uikit.R.drawable.ic_movie_24dp
        in audio -> org.emunix.nullpointer.uikit.R.drawable.ic_music_24dp
        in archives -> org.emunix.nullpointer.uikit.R.drawable.ic_folder_zip_24dp
        in books -> org.emunix.nullpointer.uikit.R.drawable.ic_book_24dp
        in pdf -> org.emunix.nullpointer.uikit.R.drawable.ic_pdf_24dp
        else -> org.emunix.nullpointer.uikit.R.drawable.ic_file_24dp
    }
}