package com.dezdeqness.presentation.features.animesimilar.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemSimilarBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.SimilarUiModel

fun similarAdapterDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<SimilarUiModel, AdapterItem, ItemSimilarBinding>(
        modelClass = SimilarUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemSimilarBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id, title = item.name))
            }

            bind {
                with(binding) {

                    animeTitle.text = item.name
                    animeBriefInfo.text = item.briefInfo
                    animeScore.text = item.score

                    Glide.with(root)
                        .clear(animeLogo)

                    Glide
                        .with(root)
                        .load(item.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .into(animeLogo)

                }

            }
        }
    )
