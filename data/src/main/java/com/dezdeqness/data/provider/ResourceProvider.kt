package com.dezdeqness.data.provider

import android.content.Context

class ResourceProvider(private val context: Context) {

    fun getString(id: String): String = getStringByIdName(id)

    fun getString(id: Int) =
        context.getString(id)

    private fun getStringByIdName(idName: String): String {
        val res = context.resources
        return res.getString(res.getIdentifier(idName, "string", context.packageName))
    }

}
