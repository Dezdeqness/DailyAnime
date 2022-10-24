package com.dezdeqness.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SingleLineSpacingItemDecoration(
    private val spacing: Int = 15,
    private val doubleFirstLastMargin: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val firstLastMargin = if (doubleFirstLastMargin) spacing * 2 else spacing

        val position = parent.getChildLayoutPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val size = parent.adapter?.itemCount ?: 0
        if (position == 0) {
            outRect.left = firstLastMargin
            outRect.right = spacing
        } else if (position < size - 1) {
            outRect.right = spacing
        } else {
            outRect.right = firstLastMargin
        }

    }

}
