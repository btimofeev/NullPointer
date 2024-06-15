package org.emunix.nullpointer.uploader.domain

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
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
        coEvery { uploadRepository.upload(any()) } returns "http://mock.srv/abc.txt"
        val inputStream = getResourceAsInputStream("abc.txt")

        val res = checkAndUploadFileUseCase.invoke(
            fileName = "abc.txt",
            stream = inputStream
        ).getOrNull()

        assert(res != null)
        assert(res?.name == "abc.txt")
        assert(res?.url == "http://mock.srv/abc.txt")
    }

    private fun getResourceAsInputStream(path: String): InputStream =
        javaClass.classLoader?.getResourceAsStream(path) ?: throw FileNotFoundException("File not found: $path")
}