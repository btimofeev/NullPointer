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
import org.emunix.nullpointer.uploader.di.UploadComponent

class UploadFragment : Fragment() {

    private val viewModel: UploadViewModel by activityViewModels {
        val appProvider = (requireActivity().application as AppProviderHolder).appProvider
        val uploadComponent = UploadComponent.create(appProvider)
        UploadViewModelFactory(
            repository = uploadComponent.getUploadRepository(),
            history = appProvider.getDatabaseRepository(),
        )
    }

    private var _binding: FragmentUploadBinding? = null

    private val binding get() = _binding!!

    private val selectFileResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val document = DocumentFile.fromSingleUri(requireContext(), uri)
                val fileName = document?.name.orEmpty()
                context?.contentResolver?.openInputStream(uri)?.let { inputStream ->
                    viewModel.uploadFile(fileName, inputStream)
                }
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
        binding.uploadButton.setOnClickListener { selectFile() }
        setupObservers()
        setupToolbar()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.url.collect { url ->
                        if (url != null) {
                            binding.textUpload.text = url
                        }
                    }
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

    companion object {

        fun newInstance() = UploadFragment()
    }
}