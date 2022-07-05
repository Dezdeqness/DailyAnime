package com.dezdeqness.presentation.features.animelist

import com.bumptech.glide.Glide
import com.dezdeqness.R
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
                    Glide.with(root)
                        .clear(animeLogo)

                    Glide
                        .with(root)
                        .load(item.logoUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(animeLogo)
                }
            }
        }
    )
