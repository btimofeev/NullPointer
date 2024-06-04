package org.emunix.nullpointer.history.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.emunix.nullpointer.history.databinding.HistoryItemBinding
import org.emunix.nullpointer.history.presentation.list.HistoryAdapter.ViewHolder
import org.emunix.nullpointer.history.presentation.model.HistoryItem

internal class HistoryAdapter(
    val onClickListener: (HistoryItem) -> Unit,
    val onItemRemoved: (HistoryItem) -> Unit,
): ListAdapter<HistoryItem, ViewHolder>(DiffCallback()), HistoryItemTouchListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            name.text = item.fileName
            url.text = item.url
            uploadDate.text = item.uploadDate
            root.setOnClickListener { onClickListener(item) }
        }
    }

    override fun onItemDismiss(position: Int) {
        onItemRemoved(getItem(position))
    }

    class ViewHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<HistoryItem>() {

        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}