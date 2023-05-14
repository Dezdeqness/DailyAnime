package com.dezdeqness.presentation.features.animedetails.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemScreenshotBinding
import com.dezdeqness.databinding.ItemScreenshotListBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ScreenshotUiModel
import com.dezdeqness.presentation.models.ScreenshotUiModelList
import com.dezdeqness.ui.SingleLineSpacingItemDecoration

fun screenshotsAdapterDelegate() =
    adapterDelegateViewBinding<ScreenshotUiModel, AdapterItem, ItemScreenshotBinding>(
        modelClass = ScreenshotUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemScreenshotBinding.inflate(layoutInflater, parent, false)
        },
        block = {
            bind {
                with(binding.screenshot) {

                    Glide
                        .with(this)
                        .clear(this)

                    Glide
                        .with(this)
                        .load(item.url)
                        .centerCrop()
                        .placeholder(R.drawable.ic_search_placeholder)
                        .into(this)
                }
            }
        }
    )

fun screenshotsAdapterListDelegate() =
    adapterDelegateViewBinding<ScreenshotUiModelList, AdapterItem, ItemScreenshotListBinding>(
        modelClass = ScreenshotUiModelList::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemScreenshotListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.screenshots.adapter = SingleListAdapter(screenshotsAdapterDelegate())
            binding.screenshots.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                (binding.screenshots.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
