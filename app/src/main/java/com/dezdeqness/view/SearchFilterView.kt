package com.dezdeqness.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.children
import com.dezdeqness.R
import com.dezdeqness.databinding.SingleChipBinding
import com.dezdeqness.databinding.ViewFilterContainerBinding
import com.dezdeqness.presentation.models.AnimeCell
import com.dezdeqness.presentation.models.AnimeSearchFilter
import com.dezdeqness.presentation.models.CellState
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

    fun setFilterData(animeSearchFilter: AnimeSearchFilter) {
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

    fun setSearchChipListener(searchChipListener: SearchChipListener) {
        chipListener = searchChipListener
    }

    fun removeSearchChipListener() {
        chipListener = null
    }

    fun updateChip(item: AnimeCell?): Boolean {
        if (item == null) {
            return false
        }
        var isUpdated = false
        binding
            .container
            .children
            .filterIsInstance<Chip>()
            .find { it.tag == item.id }
            ?.let { chip ->
                setChipState(chip, item)
                isUpdated = true
            }
        return isUpdated
    }

    private fun setupTitle(displayName: String) {
        binding.title.text = displayName
    }

    private fun onCreate() {
        binding.title.text = titleText
    }

    private fun createChip(item: AnimeCell) =
        SingleChipBinding.inflate(LayoutInflater.from(context)).root.apply {
            tag = item.id
            text = item.displayName
            setOnClickListener {
                chipListener?.onClickListener(item)
            }
            setOnCloseIconClickListener {
                chipListener?.onLongClickListener(item)
            }
            setChipState(this, item)
        }

    private fun setChipState(chip: Chip, item: AnimeCell) {
        when (item.state) {
            CellState.INCLUDE -> {
                chip.setTextColor(ContextCompat.getColor(context, R.color.pure_white))
                chip.setChipBackgroundColorResource(R.color.purple_500)
                chip.setCloseIconResource(R.drawable.ic_plus)
                chip.setCloseIconTintResource(R.color.pure_white)
                chip.isCloseIconVisible = true
            }
            CellState.EXCLUDE -> {
                chip.setTextColor(ContextCompat.getColor(context, R.color.pure_white))
                chip.setChipBackgroundColorResource(R.color.purple_500)
                chip.setCloseIconResource(R.drawable.ic_minus)
                chip.setCloseIconTintResource(R.color.pure_white)
                chip.isCloseIconVisible = true
            }
            else -> {
                chip.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                chip.setChipBackgroundColorResource(R.color.chip_background)
                chip.isCloseIconVisible = false
            }
        }
    }

    interface SearchChipListener {

        fun onClickListener(item: AnimeCell)

        fun onLongClickListener(item: AnimeCell)

    }

}
