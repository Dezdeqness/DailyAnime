package com.dezdeqness.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.dezdeqness.R
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class RibbonStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {
    private val ribbonLayout: ChipGroup

    init {
        LayoutInflater.from(context).inflate(R.layout.ribbon_status_view, this, true)
        ribbonLayout = findViewById(R.id.ribbon_layout)

        context.obtainStyledAttributes(
            attrs,
            R.styleable.RibbonStatusView,
            defStyleAttr,
            0
        ).use {
//            val paddingHorizontal = it.getDimension(
//                R.styleable.RibbonView_android_paddingHorizontal,
//                resources.getDimension(R.dimen.size_14)
//            )
//
//            val paddingVertical = it.getDimension(
//                R.styleable.RibbonView_android_paddingVertical,
//                resources.getDimension(R.dimen.size_6)
//            )
//
//            setPadding(
//                paddingHorizontal.toInt(),
//                paddingVertical.toInt(),
//                paddingHorizontal.toInt(),
//                paddingVertical.toInt()
//            )
        }
    }

    fun populate(list: List<RibbonStatusUiModel>, listener: (String) -> Unit) {
        isVisible = list.isNotEmpty()
        ribbonLayout.removeAllViews()

        list.forEach { uiModel ->
            val chip = LayoutInflater
                .from(context)
                .inflate(R.layout.item_ribbon_button, ribbonLayout, false) as Chip
            with(chip) {
                text = uiModel.displayName
                isChecked = uiModel.isSelected
                setOnClickListener {
                    listener.invoke(uiModel.id)
                }
            }
            ribbonLayout.addView(chip)
        }
    }
}
