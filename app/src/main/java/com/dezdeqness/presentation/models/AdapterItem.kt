package com.dezdeqness.presentation.models

sealed class AdapterItem {

    open fun id() = this.toString()

    open fun payload(other: Any): Payloadable = Payloadable.None

    interface Payloadable {
        object None : Payloadable
    }

}
