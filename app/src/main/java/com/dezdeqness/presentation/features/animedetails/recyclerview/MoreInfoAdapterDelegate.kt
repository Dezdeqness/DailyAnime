package com.dezdeqness.presentation.features.animedetails.recyclerview

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemMoreInfoBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.MoreInfoUiModel
import okhttp3.internal.notify

fun moreInfoAdapterDelegate(
    onSimilarClicked: () -> Unit,
    onChronologyClicked: () -> Unit,
    onStatsClicked: () -> Unit,
) =
    adapterDelegateViewBinding<MoreInfoUiModel, AdapterItem, ItemMoreInfoBinding>(
        modelClass = MoreInfoUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemMoreInfoBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.similar.setOnClickListener {
                onSimilarClicked.invoke()
            }

            binding.chronology.setOnClickListener {
                onChronologyClicked.invoke()
            }

            binding.stats.setOnClickListener {
                onStatsClicked.invoke()
            }

        }
    )
