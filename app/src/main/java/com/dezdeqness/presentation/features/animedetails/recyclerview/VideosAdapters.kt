package com.dezdeqness.presentation.features.animedetails.recyclerview

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemVideoBinding
import com.dezdeqness.databinding.ItemVideoListBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.VideoUiModel
import com.dezdeqness.presentation.models.VideoUiModelList
import com.dezdeqness.ui.SingleLineSpacingItemDecoration


fun videosAdapterDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<VideoUiModel, AdapterItem, ItemVideoBinding>(
        modelClass = VideoUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemVideoBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.VideoClick(item.sourceUrl))
            }

            bind {
                with(binding) {
                    videoName.text = item.name
                    videoSource.text = item.source

                    Glide
                        .with(video)
                        .clear(video)

                    Glide
                        .with(video)
                        .load(item.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_search_placeholder)
                        .into(video)
                }
            }
        }
    )

fun videosAdapterListDelegate(actionListener: ActionListener) =
    adapterDelegateViewBinding<VideoUiModelList, AdapterItem, ItemVideoListBinding>(
        modelClass = VideoUiModelList::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemVideoListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.videos.adapter = SingleListAdapter(videosAdapterDelegate(actionListener))
            binding.videos.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                (binding.videos.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
