package com.dezdeqness.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.use
import androidx.core.view.children
import com.dezdeqness.R
import com.dezdeqness.databinding.SingleChipBinding
import com.dezdeqness.databinding.ViewFilterContainerBinding
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.google.android.material.chip.Chip

class SearchFilterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var titleText: String = ""

    private var chipListener: SearchChipListener? = null

    private var animeSearchFilter: AnimeSearchFilter? = null

    private val filterId: String
        get(): String = animeSearchFilter?.innerId.orEmpty()

    private var binding = ViewFilterContainerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.SearchFilterView,
            0,
            R.style.SearchFilterView
        ).use { typedArray ->
            titleText = typedArray.getString(R.styleable.SearchFilterView_filterTitle).orEmpty()
        }

        onCreate()
    }

    fun setFilter(animeSearchFilter: AnimeSearchFilter) {
        this.animeSearchFilter = animeSearchFilter
        with(binding) {
            setupTitle(animeSearchFilter.displayName)
            container.removeAllViews()
            animeSearchFilter.items.forEach { cell ->
                val chip = createChip(cell)
                container.addView(chip)
            }
        }
    }

    private fun setupTitle(displayName: String) {
        binding.title.text = displayName
    }

    fun setSearchChipListener(searchChipListener: SearchChipListener) {
        chipListener = searchChipListener
    }

    fun removeSearchChipListener() {
        chipListener = null
    }

    // TODO: Add state
    fun updateChip(value: String) {
        binding
            .container
            .children
            .filterIsInstance<Chip>()
            .find { it.text == value }
            ?.setChipBackgroundColorResource(R.color.chip_selected_state)

    }

    private fun onCreate() {
        binding.title.text = titleText
    }

    private fun createChip(item: AnimeCell) =
        SingleChipBinding.inflate(LayoutInflater.from(context)).root.apply {
            text = item.displayName
            setOnClickListener {
                chipListener?.onClickListener(filterId, item.id)
            }
            setOnLongClickListener {
                chipListener?.onLongClickListener(filterId, item.id)
                true
            }
        }


    interface SearchChipListener {

        fun onClickListener(itemId: String, cellId: Int)

        fun onLongClickListener(itemId: String, cellId: Int)

    }

}
