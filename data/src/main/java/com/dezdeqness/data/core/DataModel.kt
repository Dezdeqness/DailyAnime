package com.dezdeqness.data.core

sealed interface DataModel {
    interface Api : DataModel
    interface Db : DataModel
}
