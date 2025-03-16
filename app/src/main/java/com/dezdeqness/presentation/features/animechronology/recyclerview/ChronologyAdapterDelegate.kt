package com.dezdeqness.presentation.features.animechronology.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemChronologyBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.ChronologyUiModel


fun chronologyAdapterDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<ChronologyUiModel, AdapterItem, ItemChronologyBinding>(
        modelClass = ChronologyUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemChronologyBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id, title = item.name))
            }

            bind {
                with(binding) {

                    animeTitle.text = item.name
                    animeBriefInfo.text = item.briefInfo

                    Glide.with(root)
                        .clear(animeLogo)

                    Glide
                        .with(root)
                        .load(item.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .into(animeLogo)
                }

            }
        }
    )
