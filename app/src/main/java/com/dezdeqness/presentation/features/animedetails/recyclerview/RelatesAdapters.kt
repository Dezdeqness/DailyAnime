package com.dezdeqness.presentation.features.animedetails.recyclerview

import android.widget.Toast
import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemRelatedBinding
import com.dezdeqness.databinding.ItemRelatedListBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.RelatedItemListUiModel
import com.dezdeqness.presentation.models.RelatedItemUiModel
import com.dezdeqness.ui.SingleLineSpacingItemDecoration

fun relatesAdapterDelegate() =
    adapterDelegateViewBinding<RelatedItemUiModel, AdapterItem, ItemRelatedBinding>(
        modelClass = RelatedItemUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRelatedBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, item.title, Toast.LENGTH_SHORT).show()
            }

            bind {
                with(binding) {
                    relatedName.text = item.title
                    relatedType.text = item.anime.kind + " • " + item.anime.airedYear

                    Glide
                        .with(image)
                        .clear(image)

                    Glide
                        .with(image)
                        .load(item.anime.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(image)
                }
            }
        }
    )

fun relatesAdapterListDelegate() =
    adapterDelegateViewBinding<RelatedItemListUiModel, AdapterItem, ItemRelatedListBinding>(
        modelClass = RelatedItemListUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRelatedListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.relates.adapter = SingleListAdapter(relatesAdapterDelegate())
            binding.relates.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                (binding.relates.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
