package org.emunix.nullpointer.ui.upload

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.emunix.nullpointer.R
import org.emunix.nullpointer.databinding.FragmentUploadBinding

class UploadFragment : Fragment() {

    private val viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)

    private var _binding: FragmentUploadBinding? = null

    private val binding get() = _binding!!

    private val selectFileResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                binding.textUpload.text = uri.toString()
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
}