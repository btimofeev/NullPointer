package org.emunix.nullpointer.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import org.emunix.nullpointer.uikit.model.Action
import org.emunix.nullpointer.uikit.model.Action.CopyLink
import org.emunix.nullpointer.uikit.model.Action.ShareLink
import org.emunix.nullpointer.uikit.utils.copyToClipboard
import org.emunix.nullpointer.uikit.utils.handleSystemBarInsets
import org.emunix.nullpointer.uikit.utils.shareText

internal class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding? = null

    private var isClearHistoryMenuVisible = false

    private val listAdapter: HistoryAdapter = HistoryAdapter(
        onClickListener = { item -> viewModel.onItemClick(item) },
        onItemRemoved = { item -> viewModel.onRemoveHistoryItem(item) },
    )

    private val viewModel: HistoryViewModel by viewModels {
        val appProvider = (requireActivity().application as AppProviderHolder).appProvider
        HistoryViewModelFactory(
            history = appProvider.getDatabaseRepository(),
            preferencesProvider = appProvider.getPreferencesProvider(),
        )
    }

    private val historyMenuProvider = object : MenuProvider {

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            val clearHistoryItem = menu.findItem(R.id.action_clear_history)
            clearHistoryItem?.isVisible = isClearHistoryMenuVisible
        }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_history, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
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
        val fragment = FragmentHistoryBinding.inflate(inflater, container, false)
        binding = fragment
        return fragment.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        binding?.setupToolbar()
        binding?.setupList()
        setupObservers()
    }

    private fun FragmentHistoryBinding.setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.handleSystemBarInsets()
        addMenu()
    }

    private fun FragmentHistoryBinding.setupList() {
        with(list) {
            val listLayoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            layoutManager = listLayoutManager
            adapter = listAdapter
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
                        if (items != null) binding?.showHistory(items)
                    }
                }

                launch {
                    viewModel.isSwipeToDeleteEnabled.collect { isEnabled ->
                        if (isEnabled) setSwipeToDeleteEnabled()
                    }
                }

                launch {
                    viewModel.command.collect { performAction(it) }
                }
            }
        }
    }

    private fun addMenu() {
        (activity as? MenuHost)?.addMenuProvider(
            historyMenuProvider,
            viewLifecycleOwner,
            State.RESUMED
        )
    }

    private fun updateMenu() {
        (activity as? MenuHost)?.invalidateMenu()
    }

    private fun FragmentHistoryBinding.showHistory(items: List<HistoryItem>) {
        if (items.isEmpty()) {
            emptyHistory.isVisible = true
            emptyHistoryAnim.isVisible = true
            emptyHistoryAnim.playAnimation()
            list.isVisible = false
            isClearHistoryMenuVisible = false
        } else {
            emptyHistory.isVisible = false
            emptyHistoryAnim.isVisible = false
            emptyHistoryAnim.cancelAnimation()
            list.isVisible = true
            listAdapter.submitList(items)
            isClearHistoryMenuVisible = true
        }
        updateMenu()
    }

    private fun performAction(link: Action) {
        when (link) {
            is CopyLink -> context?.copyToClipboard(link.url)
            is ShareLink -> context?.shareText(link.url)
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

    private fun setSwipeToDeleteEnabled() {
        with(binding?.list) {
            val callback = HistoryItemTouchHelperCallback(listAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    companion object {

        fun newInstance() = HistoryFragment()
    }
}