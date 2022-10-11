package com.dezdeqness.presentation.models

sealed class AdapterItem {

    fun id() = this.toString()

    fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None : Payloadable
    }

}
