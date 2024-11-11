package org.emunix.nullpointer.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.emunix.nullpointer.settings.databinding.FragmentSettingsBinding
import org.emunix.nullpointer.uikit.utils.handleSystemBarInsets

internal class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = FragmentSettingsBinding.inflate(inflater, container, false)
        binding = fragment
        return fragment.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setupToolbar()
    }

    private fun FragmentSettingsBinding.setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.handleSystemBarInsets()
    }

    companion object {

        fun newInstance() = SettingsFragment()
    }
}