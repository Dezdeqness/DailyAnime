package com.dezdeqness.presentation.features.animelist

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemAnimeBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeUiItem

fun animeAdapterDelegate() =
    adapterDelegateViewBinding<AnimeUiItem, AdapterItem, ItemAnimeBinding>(
        modelClass = AnimeUiItem::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemAnimeBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {
                with(binding) {
                    animeBriefInfo.text = item.briefInfo
                    animeKind.text = item.kind
                }
            }
        }
    )
