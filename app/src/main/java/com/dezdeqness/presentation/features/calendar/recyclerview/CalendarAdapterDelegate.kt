package com.dezdeqness.presentation.features.calendar.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemCalendarBinding
import com.dezdeqness.databinding.ItemCalendarListBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.CalendarListUiModel
import com.dezdeqness.presentation.models.CalendarUiModel
import com.dezdeqness.ui.SingleLineSpacingItemDecoration

fun calendarAdapterDelegate(
    actionListener: ActionListener,
) =
    adapterDelegateViewBinding<CalendarUiModel, AdapterItem, ItemCalendarBinding>(
        modelClass = CalendarUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemCalendarBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id))
            }

            bind {
                val context = binding.root.context
                with(binding) {

                    Glide
                        .with(context)
                        .clear(image)

                    Glide
                        .with(context)
                        .load(item.logoUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(image)

                    calendarName.text = item.name
                    calendarInfo.text = item.episodeInfo
                }
            }
        }
    )

fun calendarListDelegate(
    actionListener: ActionListener,
) =
    adapterDelegateViewBinding<CalendarListUiModel, AdapterItem, ItemCalendarListBinding>(
        modelClass = CalendarListUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemCalendarListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.calendarItems.adapter = SingleListAdapter(calendarAdapterDelegate(
                actionListener = actionListener,
            ))
            binding.calendarItems.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                binding.title.text = item.header
                (binding.calendarItems.adapter as SingleListAdapter).submitList(item.items)
            }
        }
    )
