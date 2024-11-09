package com.dezdeqness.presentation.models

sealed class AdapterItem {

    open fun id() = this.toString()

}
