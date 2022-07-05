package com.dezdeqness.presentation.models

sealed class AdapterItem {

    interface Payloadable {
        object None : Payloadable
    }

}
