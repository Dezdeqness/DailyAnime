package com.dezdeqness.presentation.features.editrate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.dezdeqness.databinding.ItemCarouselBinding

class CarouselViewAdapter(
    private val context: Context,
    private var items: List<CarouselPicker.CarouselItem>
) : PagerAdapter() {
    private var lastSelected = 0
    private var opacity = 1f

    override fun getCount() = items.size

    fun setOpacity(opacity: Float) {
        var rawOpacity = opacity
        rawOpacity = rawOpacity.coerceAtLeast(0f)
        rawOpacity = rawOpacity.coerceAtMost(1f)
        this.opacity = rawOpacity
    }

    fun applyOpacity(position: Int) {
        if (lastSelected < items.size) {
            items[lastSelected].binding?.root?.alpha = opacity
        }

        items[position].binding?.root?.alpha = 1f
        lastSelected = position
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(context))
        val pickerItem = items[position]
        pickerItem.binding = binding
        if (position != lastSelected) {
            binding.root.alpha = opacity
        }

        binding.item.text = pickerItem.text

        binding.item.tag = position
        container.addView(binding.root)
        return binding.item
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}