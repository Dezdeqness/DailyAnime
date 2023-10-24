package com.dezdeqness.presentation.features.animedetails.recyclerview

import android.content.res.ColorStateList
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemCellsBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeCellList
import com.google.android.material.chip.Chip

fun cellsAdapterDelegate() =
    adapterDelegateViewBinding<AnimeCellList, AdapterItem, ItemCellsBinding>(
        modelClass = AnimeCellList::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemCellsBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    chipGroup.removeAllViews()
                    item.list.forEach {
                        chipGroup.addView(
                            Chip(chipGroup.context).apply {
                                text = it.displayName
                                chipStrokeWidth = 1F
                                chipStrokeColor =
                                    ColorStateList.valueOf(context.getColor(android.R.color.black))
                                isCloseIconVisible = false
                            }
                        )
                    }
                }
            }
        }
    )
