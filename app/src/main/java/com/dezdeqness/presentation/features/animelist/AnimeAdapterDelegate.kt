package com.dezdeqness.presentation.features.animelist

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemAnimeBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeUiModel

fun animeAdapterDelegate(listener: (Long) -> Unit) =
    adapterDelegateViewBinding<AnimeUiModel, AdapterItem, ItemAnimeBinding>(
        modelClass = AnimeUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemAnimeBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                listener(item.id)
            }

            bind {
                with(binding) {
                    animeBriefInfo.text = item.briefInfo
                    animeKind.text = item.kind
                    Glide.with(root)
                        .clear(animeLogo)

                    Glide
                        .with(root)
                        .load(item.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(animeLogo)
                }
            }
        }
    )
