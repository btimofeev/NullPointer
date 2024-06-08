package org.emunix.nullpointer.uploader.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.uikit.utils.handleSystemBarInsets
import org.emunix.nullpointer.uploader.R
import org.emunix.nullpointer.uploader.databinding.FragmentUploadBinding
import org.emunix.nullpointer.uploader.presentation.model.ScreenState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.ChooseFileState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadFailure
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadInProgressState
import org.emunix.nullpointer.uploader.presentation.model.ScreenState.UploadSuccess
import org.emunix.nullpointer.uploader.work.UploadWorkManagerImpl

class UploadFragment : Fragment() {

    private val viewModel: UploadViewModel by activityViewModels {
        val appProvider = (requireActivity().application as AppProviderHolder).appProvider
        UploadViewModelFactory(
            uploadWorkManager = UploadWorkManagerImpl(appProvider.getContext())
        )
    }

    private var _binding: FragmentUploadBinding? = null

    private val binding get() = _binding!!

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
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseFileButton.setOnClickListener { selectFile() }
        setupObservers()
        setupToolbar()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.screenState.collect { changeScreenState(it) }
                }
            }
        }
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.handleSystemBarInsets()
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
            is ChooseFileState -> binding.setupChooseFileScreenState()
            is UploadInProgressState -> binding.setupUploadInProgressScreenState()
            is UploadSuccess -> binding.setupUploadSuccessScreenState(state.url)
            is UploadFailure -> binding.setupUploadFailureScreenState()
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
        copyToClipboardButton.setOnClickListener { copyToClipboard(url) }
        shareButton.setOnClickListener { share(url) }
    }

    private fun FragmentUploadBinding.setupUploadFailureScreenState() {
        anim.setAnimation("error.lottie")
        anim.playAnimation()
        chooseFileButton.isVisible = true
        copyToClipboardButton.isVisible = false
        shareButton.isVisible = false
        tryAgainButton.isVisible = true
        tryAgainButton.setOnClickListener { viewModel.tryAgain() }
        cancelButton.isVisible = false
        mainText.isVisible = true
        mainText.text = getText(R.string.upload_failed)
    }

    private fun copyToClipboard(text: String) {
        context?.let { ctx ->
            val clipboard: ClipboardManager? = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(text, text)
            clipboard?.setPrimaryClip(clip)
        }
    }

    private fun share(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {

        fun newInstance() = UploadFragment()
    }
}