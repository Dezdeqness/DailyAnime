package com.dezdeqness.presentation.models

import com.google.common.collect.ImmutableList

data class RoleUiModel(
    val name: String,
    val imageUrl: String,
) : AdapterItem()

data class RoleUiModelList(val list: ImmutableList<RoleUiModel>) : AdapterItem()
