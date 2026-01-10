package com.dezdeqness.core.provider

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getString(id: String): String = getStringByIdName(id)

    fun getString(@StringRes id: Int) =
        context.getString(id)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any) =
        context.getString(resId, *formatArgs)

    @Deprecated("")
    private fun getStringByIdName(idName: String): String {
        val res = context.resources
        return res.getString(res.getIdentifier(idName, "string", context.packageName))
    }

}
