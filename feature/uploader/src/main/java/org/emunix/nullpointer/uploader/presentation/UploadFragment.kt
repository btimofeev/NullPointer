package org.emunix.nullpointer.uploader.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.uikit.model.Action
import org.emunix.nullpointer.uikit.model.Action.CopyLink
import org.emunix.nullpointer.uikit.model.Action.ShareLink
import org.emunix.nullpointer.uikit.utils.copyToClipboard
import org.emunix.nullpointer.uikit.utils.handleSystemBarInsets
import org.emunix.nullpointer.uikit.utils.shareText
import org.emunix.nullpointer.uploader.R
import org.emunix.nullpointer.uploader.databinding.FragmentUploadBinding
import org.emunix.nullpointer.core.api.domain.ErrorType
import org.emunix.nullpointer.core.api.domain.ErrorType.FORBIDDEN_FILE_FORMAT
import org.emunix.nullpointer.core.api.domain.ErrorType.MAX_FILE_SIZE_HAS_BEEN_EXCEEDED
import org.emunix.nullpointer.core.api.domain.ErrorType.UPLOAD_FAILED
import org.emunix.nullpointer.uploader.presentation.model.ScreenState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.ChooseFileState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.Error
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadInProgressState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadSuccess

internal class UploadFragment : Fragment() {

    private val viewModel: UploadViewModel by activityViewModels {
        val appProvider = (requireActivity().application as AppProviderHolder).appProvider
        UploadViewModelFactory(
            uploadWorkManager = appProvider.getUploadWorkManager(),
            preferencesProvider = appProvider.getPreferencesProvider(),
        )
    }

    private var binding: FragmentUploadBinding? = null

    private val selectFileResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val document = DocumentFile.fromSingleUri(requireContext(), uri)
                val fileName = document?.name.orEmpty()
                viewModel.uploadFile(fileName, uri.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = FragmentUploadBinding.inflate(inflater, container, false)
        binding = fragment
        return fragment.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.chooseFileButton?.setOnClickListener { selectFile() }
        setupObservers()
        binding?.setupToolbar()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.screenState.collect { changeScreenState(it) }
                }

                launch {
                    viewModel.command.collect { performAction(it) }
                }
                launch {
                    viewModel.showUploadFileQuestionDialog.collect {
                        showUploadFileQuestionDialog(it)
                    }
                }
            }
        }
    }

    private fun FragmentUploadBinding.setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.handleSystemBarInsets()
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        try {
            selectFileResult.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), R.string.error_choose_file_app_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeScreenState(state: ScreenState) {
        when (state) {
            is ChooseFileState -> binding?.setupChooseFileScreenState()
            is UploadInProgressState -> binding?.setupUploadInProgressScreenState()
            is UploadSuccess -> binding?.setupUploadSuccessScreenState(state.url)
            is Error -> binding?.setupErrorScreenState(state.type)
        }
    }

    private fun FragmentUploadBinding.setupChooseFileScreenState() {
        anim.setAnimation("choose_file.lottie")
        anim.playAnimation()
        chooseFileButton.isVisible = true
        copyToClipboardButton.isVisible = false
        shareButton.isVisible = false
        tryAgainButton.isVisible = false
        cancelButton.isVisible = false
        mainText.isVisible = false
    }

    private fun FragmentUploadBinding.setupUploadInProgressScreenState() {
        anim.setAnimation("upload_in_progress.lottie")
        anim.playAnimation()
        chooseFileButton.isVisible = false
        copyToClipboardButton.isVisible = false
        shareButton.isVisible = false
        tryAgainButton.isVisible = false
        cancelButton.isVisible = true
        cancelButton.setOnClickListener { viewModel.cancelUpload() }
        mainText.isVisible = true
        mainText.text = getText(R.string.please_wait)
    }

    private fun FragmentUploadBinding.setupUploadSuccessScreenState(url: String) {
        anim.setAnimation("share.lottie")
        anim.playAnimation()
        chooseFileButton.isVisible = true
        copyToClipboardButton.isVisible = true
        shareButton.isVisible = true
        tryAgainButton.isVisible = false
        cancelButton.isVisible = false
        mainText.isVisible = true
        mainText.text = getString(R.string.upload_success, url)
        copyToClipboardButton.setOnClickListener { context?.copyToClipboard(url) }
        shareButton.setOnClickListener { context?.shareText(url) }
    }

    private fun FragmentUploadBinding.setupErrorScreenState(errorType: ErrorType) {
        anim.setAnimation("error.lottie")
        anim.playAnimation()
        chooseFileButton.isVisible = true
        copyToClipboardButton.isVisible = false
        shareButton.isVisible = false
        tryAgainButton.isVisible = true
        tryAgainButton.setOnClickListener { viewModel.tryAgain() }
        cancelButton.isVisible = false
        mainText.isVisible = true
        mainText.text = when (errorType) {
            UPLOAD_FAILED -> getText(R.string.upload_failed)
            MAX_FILE_SIZE_HAS_BEEN_EXCEEDED -> getText(R.string.error_max_file_size_exceeds)
            FORBIDDEN_FILE_FORMAT -> getText(R.string.error_forbidden_file_format)
        }
    }

    private fun performAction(action: Action) {
        when(action) {
            is CopyLink -> context?.copyToClipboard(action.url)
            is ShareLink -> context?.shareText(action.url)
        }
    }

    private fun showUploadFileQuestionDialog(launchUri: String?) {
        if (launchUri == null) return
        context?.let { ctx ->
            val uri = launchUri.toUri()
            val document = DocumentFile.fromSingleUri(ctx, uri)
            val fileName = document?.name.orEmpty()
            MaterialAlertDialogBuilder(ctx)
                .setTitle(R.string.do_you_want_to_upload_file)
                .setMessage(fileName)
                .setPositiveButton(R.string.upload) { _, _ ->
                    viewModel.uploadFile(fileName, launchUri)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setOnDismissListener {
                    viewModel.onUploadFileQuestionDialogDismiss()
                }
                .create()
                .show()
        }
    }

    companion object {

        fun newInstance() = UploadFragment()
    }
}