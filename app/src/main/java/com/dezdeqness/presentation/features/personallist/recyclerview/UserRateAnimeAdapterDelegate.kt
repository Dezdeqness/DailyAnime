package com.dezdeqness.presentation.features.personallist.recyclerview

import com.bumptech.glide.Glide
import com.dezdeqness.R
import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.databinding.ItemUserRateAnimeBinding
import com.dezdeqness.presentation.action.Action
import com.dezdeqness.presentation.action.ActionListener
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.presentation.models.UserRateUiModel

fun userRateAnimeAdapterDelegate(
    actionListener: ActionListener,
    onEditRateClicked: (Long) -> Unit,
) =
    adapterDelegateViewBinding<UserRateUiModel, AdapterItem, ItemUserRateAnimeBinding>(
        modelClass = UserRateUiModel::class.java,
        viewBinding = { layoutInflater, parent ->
            ItemUserRateAnimeBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            binding.root.setOnClickListener {
                actionListener.onActionReceive(Action.AnimeClick(animeId = item.id))
            }

            binding.animeChangeRate.setOnClickListener {
                onEditRateClicked.invoke(item.rateId)
            }

            bind { payloads ->
                with(binding) {
                    if (payloads.isEmpty()) {
                        animeTitle.text = item.name
                        animeBriefInfo.text = item.briefInfo
                        animeProgress.text = item.progress
                        animeScore.text = item.score

                        Glide.with(root)
                            .clear(animeLogo)

                        Glide
                            .with(root)
                            .load(item.logoUrl)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(animeLogo)
                    } else {
                        payloads
                            .filterIsInstance<UserRateUiModel.UserRateUiModelPayload>()
                            .forEach { payload ->
                                payload.name?.let {
                                    animeTitle.text = item.name
                                }
                                payload.briefInfo?.let {
                                    animeBriefInfo.text = item.briefInfo
                                }
                                payload.progress?.let {
                                    animeProgress.text = item.progress
                                }
                                payload.score?.let {
                                    animeScore.text = item.score
                                }

                            }
                    }
                }

            }
        }
    )
