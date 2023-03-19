package com.dezdeqness.presentation.features.animelist

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemAnimeBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.AnimeUiModel

fun animeAdapterDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<AnimeUiModel, AdapterItem, ItemAnimeBinding>(
        modelClass = AnimeUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemAnimeBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id))
            }

            bind {
                with(binding) {
                    animeTitle.text = item.title
                    animeKind.text = item.kind
                    Glide.with(root)
                        .clear(animeLogo)

                    Glide
                        .with(root)
                        .load(item.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_search_placeholder)
                        .into(animeLogo)
                }
            }
        }
    )
