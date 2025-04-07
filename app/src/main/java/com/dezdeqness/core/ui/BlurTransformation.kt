package com.dezdeqness.core.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import jp.wasabeef.glide.transformations.internal.FastBlur
import jp.wasabeef.glide.transformations.internal.RSBlur

class BlurTransformation @JvmOverloads constructor(
    private val context: Context,
    private val radius: Float = DEFAULT_RADIUS,
    private val sampling: Float = DEFAULT_SAMPLING
) : Transformation {

    init {
        require(radius in 0.0..25.0) { "radius must be in [0, 25]." }
        require(sampling > 0) { "sampling must be > 0." }
    }

    override val cacheKey = "${BlurTransformation::class.java.name}-$radius-$sampling"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val scaledWidth = (input.width / sampling).toInt()
        val scaledHeight = (input.height / sampling).toInt()
        val output =
            createBitmap(scaledWidth, scaledHeight, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            scale(1 / sampling, 1 / sampling)
            drawBitmap(input, 0f, 0f, paint)
        }

        return try {
            RSBlur.blur(context, output, radius.toInt())
        } catch (e: Exception) {
            FastBlur.blur(output, radius.toInt(), true)
        }
    }

    private companion object {
        private const val DEFAULT_RADIUS = 25f
        private const val DEFAULT_SAMPLING = 1f
    }
}