package org.emunix.nullpointer.history.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.history.R
import org.emunix.nullpointer.history.databinding.FragmentHistoryBinding
import org.emunix.nullpointer.history.presentation.list.HistoryAdapter
import org.emunix.nullpointer.history.presentation.list.HistoryItemTouchHelperCallback
import org.emunix.nullpointer.history.presentation.model.HistoryItem

internal class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    private val listAdapter: HistoryAdapter = HistoryAdapter(
        onClickListener = { item -> onItemClick(item) },
        onItemRemoved = { item -> viewModel.onRemoveHistoryItem(item) },
    )

    private val viewModel: HistoryViewModel by viewModels {
        val appProvider = (requireActivity().application as AppProviderHolder).appProvider
        HistoryViewModelFactory(
            history = appProvider.getDatabaseRepository(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        setupList()
        setupObservers()
    }

    private fun setupList() {
        with(binding.list) {
            val listLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            layoutManager = listLayoutManager
            adapter = listAdapter
            val callback = HistoryItemTouchHelperCallback(listAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(context, listLayoutManager.orientation))
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(State.STARTED) {
                launch {
                    viewModel.historyItems.collect { items ->
                        showHistory(items)
                    }
                }
            }
        }
    }

    private fun showHistory(items: List<HistoryItem>) {
        binding.emptyHistory.isVisible = items.isEmpty()
        binding.list.isVisible = items.isNotEmpty()
        listAdapter.submitList(items)
    }

    private fun onItemClick(item: HistoryItem) {
        context?.let { ctx ->
            val clipboard: ClipboardManager? = ctx.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(item.url, item.url)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(ctx, getString(R.string.url_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance() = HistoryFragment()
    }
}