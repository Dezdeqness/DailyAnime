package com.dezdeqness.presentation.features.animedetails.recyclerview

import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemAnimeHeaderBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.HeaderItemUiModel
import jp.wasabeef.glide.transformations.BlurTransformation

private const val BLUR_RADIUS = 25

fun headerItemAdapterDelegate() =
    adapterDelegateViewBinding<HeaderItemUiModel, AdapterItem, ItemAnimeHeaderBinding>(
        modelClass = HeaderItemUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemAnimeHeaderBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    nameView.text = item.title
                    score.text = item.ratingScore.toString()

                    Glide.with(binding.root).clear(backgroundImage)
                    Glide.with(binding.root).clear(imageView)

                    Glide
                        .with(backgroundImage)
                        .load(item.imageUrl)
                        .apply(
                            RequestOptions.bitmapTransform(
                                MultiTransformation(
                                    CenterCrop(),
                                    BlurTransformation(BLUR_RADIUS),
                                ),
                            )
                        )
                        .into(backgroundImage)

                    Glide
                        .with(imageView)
                        .load(item.imageUrl)
                        .centerCrop()
                        .into(imageView)
                }
            }
        }
    )
