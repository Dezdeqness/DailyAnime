package com.dezdeqness.presentation.features.editrate

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dezdeqness.databinding.ItemCarouselBinding
import kotlin.math.abs


class CarouselPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private var itemsVisible = 7
    private var divisor = 0f
    private var opacity = 0.5f

    init {
        divisor = 1 + 1f / (itemsVisible - 1)
        init()
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                (adapter as? CarouselAdapter)?.applyOpacity(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val childHeight = child.measuredHeight
            if (childHeight > height) height = childHeight
        }

        val heightMeasure = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasure)
        val width = measuredWidth
        pageMargin = (-width / divisor).toInt()
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        this.offscreenPageLimit = adapter?.count ?: 5

        (adapter as? CarouselAdapter)?.apply {
            setOpacity(opacity)
        }
    }

    private fun init() {
        this.setPageTransformer(false, CustomPageTransformer())
        this.clipChildren = false
        setFadingEdgeLength(0)
    }

    class CarouselItem(val text: String, var binding: ItemCarouselBinding? = null)

    class CustomPageTransformer : PageTransformer {

        override fun transformPage(view: View, position: Float) {
            view.scaleY = 1 - abs(position) * 1.5f
            view.scaleX = 1 - abs(position) * 1.5f
        }
    }
}
