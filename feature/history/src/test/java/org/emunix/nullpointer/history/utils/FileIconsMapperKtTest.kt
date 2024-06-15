package org.emunix.nullpointer.history.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FileIconsMapperKtTest(private val filename: String, private val res: Int) {

    @Test
    fun getIconResForFile() {
        assert(getIconResForFile(filename) == res)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf("/file/1.avi", org.emunix.nullpointer.uikit.R.drawable.ic_movie_24dp),
            arrayOf("/docs/doc.pdf", org.emunix.nullpointer.uikit.R.drawable.ic_pdf_24dp),
            arrayOf("/my/music/bach.mp3", org.emunix.nullpointer.uikit.R.drawable.ic_music_24dp),
            arrayOf("wallpaper.png", org.emunix.nullpointer.uikit.R.drawable.ic_image_24dp),
            arrayOf("backup.tar.gz", org.emunix.nullpointer.uikit.R.drawable.ic_folder_zip_24dp),
            arrayOf("/books/dorian_gray.epub", org.emunix.nullpointer.uikit.R.drawable.ic_book_24dp),
            arrayOf("/books/file.unknown", org.emunix.nullpointer.uikit.R.drawable.ic_file_24dp),
        )
    }
}