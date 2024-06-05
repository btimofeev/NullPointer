package org.emunix.nullpointer.history.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.history.R
import org.emunix.nullpointer.history.databinding.FragmentHistoryBinding
import org.emunix.nullpointer.history.presentation.list.HistoryAdapter
import org.emunix.nullpointer.history.presentation.list.HistoryItemTouchHelperCallback
import org.emunix.nullpointer.history.presentation.model.HistoryItem
import org.emunix.nullpointer.uikit.utils.handleSystemBarInsets

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

    private val historyMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_history, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when(menuItem.itemId){
                R.id.action_clear_history -> {
                    onClearHistoryClick()
                    true
                }
                else -> false
            }
        }
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
        setupToolbar()
        setupList()
        setupObservers()
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.handleSystemBarInsets()
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

            ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
                WindowInsetsCompat.CONSUMED
            }
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

    private fun showMenu() {
        (activity as? MenuHost)?.addMenuProvider(historyMenuProvider, viewLifecycleOwner, State.RESUMED)
    }

    private fun hideMenu() {
        (activity as? MenuHost)?.removeMenuProvider(historyMenuProvider)
    }

    private fun showHistory(items: List<HistoryItem>) {
        if (items.isEmpty()) {
            binding.emptyHistory.isVisible = true
            binding.list.isVisible = false
            hideMenu()
        } else {
            binding.emptyHistory.isVisible = false
            binding.list.isVisible = true
            listAdapter.submitList(items)
            showMenu()
        }
    }

    private fun onItemClick(item: HistoryItem) {
        context?.let { ctx ->
            val clipboard: ClipboardManager? = ctx.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(item.url, item.url)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(ctx, getString(R.string.url_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClearHistoryClick() {
        context?.let { ctx ->
            MaterialAlertDialogBuilder(ctx)
                .setMessage(R.string.do_you_want_clear_history)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.onClearHistoryClick()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
    }

    companion object {

        fun newInstance() = HistoryFragment()
    }
}