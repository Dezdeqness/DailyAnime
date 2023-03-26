package com.dezdeqness.presentation.features.personallist.recyclerview

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemPersonalListFilterBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.PersonalListFilterUiModel

fun personalListFilterAdapterDelegate(
    onOrderClicked: (Boolean) -> Unit,
    onSortClicked: (String) -> Unit,
) =
    adapterDelegateViewBinding<PersonalListFilterUiModel, AdapterItem, ItemPersonalListFilterBinding>(
        modelClass = PersonalListFilterUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemPersonalListFilterBinding.inflate(layoutInflater, parent, false)
        },
        block = {
            binding.order.setOnClickListener {
                onOrderClicked(item.isAscending)
            }

            binding.sort.setOnClickListener {
                onSortClicked(item.sort.sort)
            }

            bind { payloads ->
                with(binding) {
                    if (payloads.isEmpty()) {
                        sort.text = item.sort.sort
                        order.text = if (item.isAscending) {
                            "ASC"
                        } else {
                            "DESC"
                        }
                    } else {
                        payloads
                            .filterIsInstance<PersonalListFilterUiModel.PersonalListFilterPayload>()
                            .forEach { payload ->
                                payload.sort?.let {
                                    sort.text = item.sort.sort
                                }
                                payload.isAscending?.let {
                                    order.text = if (item.isAscending) {
                                        "ASC"
                                    } else {
                                        "DESC"
                                    }
                                }

                            }
                    }
                }
            }
        }
    )