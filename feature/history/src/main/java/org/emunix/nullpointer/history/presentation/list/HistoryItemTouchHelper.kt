package org.emunix.nullpointer.history.presentation.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface HistoryItemTouchListener {

    fun onItemDismiss(position: Int)
}

class HistoryItemTouchHelperCallback(
    private val listener: HistoryItemTouchListener
) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.LEFT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        listener.onItemDismiss(viewHolder.adapterPosition)
    }
}