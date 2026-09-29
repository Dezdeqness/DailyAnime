package com.dezdeqness.contract.core

data class Page<T>(
    val items: List<T>,
    val hasNext: Boolean,
)
