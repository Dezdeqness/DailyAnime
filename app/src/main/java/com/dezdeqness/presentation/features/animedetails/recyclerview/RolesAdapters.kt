package com.dezdeqness.presentation.features.animedetails.recyclerview

import android.widget.Toast
import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.core.SingleListAdapter
import com.dezdeqness.databinding.ItemRoleBinding
import com.dezdeqness.databinding.ItemRoleListBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.RoleUiModel
import com.dezdeqness.presentation.models.RoleUiModelList
import com.dezdeqness.ui.SingleLineSpacingItemDecoration


fun roleAdapterDelegate() =
    adapterDelegateViewBinding<RoleUiModel, AdapterItem, ItemRoleBinding>(
        modelClass = RoleUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRoleBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, item.name, Toast.LENGTH_SHORT).show()
            }

            bind {
                with(binding) {

                    roleName.text = item.name

                    Glide
                        .with(roleImage)
                        .clear(roleImage)

                    Glide
                        .with(roleImage)
                        .load(item.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(roleImage)
                }
            }
        }
    )

fun roleAdapterListDelegate() =
    adapterDelegateViewBinding<RoleUiModelList, AdapterItem, ItemRoleListBinding>(
        modelClass = RoleUiModelList::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemRoleListBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.roles.adapter = SingleListAdapter(roleAdapterDelegate())
            binding.roles.addItemDecoration(SingleLineSpacingItemDecoration())

            bind {
                (binding.roles.adapter as SingleListAdapter).submitList(item.list)
            }
        }
    )
