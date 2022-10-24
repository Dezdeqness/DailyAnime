package com.dezdeqness.presentation.features.animedetails.recyclerview

import android.widget.Toast
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemBriefInfoBinding
import com.dezdeqness.databinding.ItemBriefInfoListBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.BriefInfoUiModel
import com.dezdeqness.presentation.models.BriefInfoUiModelList
import com.dezdeqness.ui.SingleLineSpacingItemDecoration


fun briefInfoAdapterDelegate() =
    adapterDelegateViewBinding<BriefInfoUiModel, AdapterItem, ItemBriefInfoBinding>(
        modelClass = BriefInfoUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemBriefInfoBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, item.title, Toast.LENGTH_SHORT).show()
            }

            bind {
                with(binding) {
                    title.text = item.title
                    info.text = item.info
                }
            }
        }
    )

fun briefInfoAdapterListDelegate() =
    adapterDelegateViewBinding<BriefInfoUiModelList, AdapterItem, ItemBriefInfoListBinding>(
        modelClass = BriefInfoUiModelList::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemBriefInfoListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.briefInfo.adapter = SingleListAdapter(briefInfoAdapterDelegate())
            binding.briefInfo.addItemDecoration(
                SingleLineSpacingItemDecoration(
                    doubleFirstLastMargin = false,
                    spacing = 30,
                )
            )

            bind {
                (binding.briefInfo.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
