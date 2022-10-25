package com.dezdeqness.presentation.models

data class RoleUiModel(
    val name: String,
    val imageUrl: String,
) : AdapterItem()

data class RoleUiModelList(
    val list: List<RoleUiModel>
) : AdapterItem()
