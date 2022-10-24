package com.dezdeqness.presentation.features.animedetails.recyclerview

import androidx.core.text.HtmlCompat
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemDescriptionBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.DescriptionUiModel

fun descriptionAdapterDelegate() =
    adapterDelegateViewBinding<DescriptionUiModel, AdapterItem, ItemDescriptionBinding>(
        modelClass = DescriptionUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemDescriptionBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    description.text = HtmlCompat.fromHtml(
                        item.content,
                        HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV
                    )
                }
            }
        }
    )