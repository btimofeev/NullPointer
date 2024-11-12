package org.emunix.nullpointer.uploader.domain

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.emunix.nullpointer.core.api.domain.FileTypeIsForbiddenException
import org.emunix.nullpointer.core.api.domain.MaxFileSizeHasBeenExceedsException
import org.emunix.nullpointer.uploader.domain.model.UploadResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.FileNotFoundException
import java.io.InputStream


class CheckAndUploadFileUseCaseImplTest {

    @get:Rule
    var folder = TemporaryFolder()

    private lateinit var uploadRepository: UploadRepository
    private lateinit var checkAndUploadFileUseCase: CheckAndUploadFileUseCase

    @Before
    fun setUp() {
        uploadRepository = mockk(relaxed = true)
        checkAndUploadFileUseCase = CheckAndUploadFileUseCaseImpl(uploadRepository, folder.root)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `check upload`() = runTest {
        coEvery { uploadRepository.upload(any()) } returns UploadResponse(
            url = "http://mock.srv/abc.txt",
            token = "123",
        )
        val inputStream = getResourceAsInputStream("abc.txt")

        val res = checkAndUploadFileUseCase.invoke(
            fileName = "abc.txt",
            stream = inputStream
        ).getOrNull()

        assert(res != null)
        assert(res?.name == "abc.txt")
        assert(res?.url == "http://mock.srv/abc.txt")
        assert(res?.token == "123")
    }

    @Test
    fun `check forbidden file type`() = runTest {
        val inputStream = getResourceAsInputStream("files.rar")

        checkAndUploadFileUseCase.invoke(
            fileName = "files.rar",
            stream = inputStream
        )
            .onSuccess { error("illegal way") }
            .onFailure { err ->
                assert(err is FileTypeIsForbiddenException)
            }
    }

    @Test
    fun `check file max size error`() = runTest {
        val bigFileSize = 536870913
        val inputStream = object : InputStream() {
            var count = 0
            override fun read(): Int {
                count++
                return if (count > bigFileSize) -1 else 0xf
            }
        }

        checkAndUploadFileUseCase.invoke(
            fileName = "big.file",
            stream = inputStream
        )
            .onSuccess { error("illegal way") }
            .onFailure { err ->
                assert(err is MaxFileSizeHasBeenExceedsException)
            }
    }

    private fun getResourceAsInputStream(path: String): InputStream =
        javaClass.classLoader?.getResourceAsStream(path) ?: throw FileNotFoundException("File not found: $path")
}