package com.dezdeqness.presentation.features.animedetails.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemRelatedBinding
import com.dezdeqness.databinding.ItemRelatedListBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.ui.SingleLineSpacingItemDecoration

fun relatesAdapterDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<RelatedItemUiModel, AdapterItem, ItemRelatedBinding>(
        modelClass = RelatedItemUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRelatedBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id))
            }

            bind {
                with(binding) {
                    relatedName.text = item.type
                    relatedType.text = item.briefInfo

                    Glide
                        .with(image)
                        .clear(image)

                    Glide
                        .with(image)
                        .load(item.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .into(image)
                }
            }
        }
    )

fun relatesAdapterListDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<RelatedItemListUiModel, AdapterItem, ItemRelatedListBinding>(
        modelClass = RelatedItemListUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRelatedListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.relates.adapter = SingleListAdapter(relatesAdapterDelegate(actionListener))
            binding.relates.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                (binding.relates.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
