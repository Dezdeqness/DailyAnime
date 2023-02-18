package com.dezdeqness.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dezdeqness.R


class RecyclerViewWithState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var recyclerLayout: ViewGroup? = null

    private val recyclerView: RecyclerView
        get() = recyclerLayout as RecyclerView

    private var emptyStateView: View? = null
    private var loadingView: View? = null
    private var errorView: View? = null

    private var recyclerViewId: Int
    private var loadingStateId: Int
    private var emptyStateId: Int
    private var errorStateId: Int

    private var isLoadingStateShowing: Boolean = false
    private var isEmptyStateShowing: Boolean = false

    var layoutManager: RecyclerView.LayoutManager?
        get() = recyclerView.layoutManager
        set(value) {
            recyclerView.layoutManager = value
        }

    var adapter: RecyclerView.Adapter<*>?
        get() = recyclerView.adapter
        set(value) {
            recyclerView.adapter = value
        }

    init {

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.RecyclerViewWithState,
            defStyleAttr,
            0
        )

        recyclerViewId = typedArray.getResourceId(R.styleable.RecyclerViewWithState_recycleViewLayout, 0)
        loadingStateId = typedArray.getResourceId(R.styleable.RecyclerViewWithState_loadingStateLayout, R.layout.state_loading)
        emptyStateId = typedArray.getResourceId(R.styleable.RecyclerViewWithState_emptyStateLayout, 0)
        errorStateId = typedArray.getResourceId(R.styleable.RecyclerViewWithState_errorStateLayout, 0)

        recyclerLayout = LayoutInflater.from(context).inflate(recyclerViewId, this, false) as ViewGroup
        loadingView = LayoutInflater.from(context).inflate(loadingStateId, this, false)
        emptyStateView = LayoutInflater.from(context).inflate(emptyStateId, this, false)
        // TODO: For future implementations
//        errorView = LayoutInflater.from(context).inflate(errorStateId, this, false)

        addView(recyclerLayout)

        typedArray.recycle()
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(decor)
    }

    fun setLoadingState(isLoadingStateShowing: Boolean) {
        if (this.isLoadingStateShowing == isLoadingStateShowing) {
            return
        }
        this.isLoadingStateShowing = isLoadingStateShowing

        removeView(loadingView)

        if (isLoadingStateShowing) {
            addView(loadingView)
        }
    }

    fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    fun setEmptyState(isEmptyStateShowing: Boolean) {
        if (this.isEmptyStateShowing == isEmptyStateShowing) {
            return
        }
        this.isEmptyStateShowing = isEmptyStateShowing
        removeView(emptyStateView)

        val gifImageView = emptyStateView?.findViewById<AppCompatImageView>(R.id.empty_screen_image)
        gifImageView?.let {
            Glide.with(this).clear(it)
        }

        if (isEmptyStateShowing) {
            gifImageView?.let {
                Glide
                    .with(this)
                    .asGif()
                    .load(R.raw.sad_cat)
                    .into(it)
            }

            addView(emptyStateView)
        }
    }

}
