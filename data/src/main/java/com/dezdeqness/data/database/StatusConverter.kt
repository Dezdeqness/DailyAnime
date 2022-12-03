package com.dezdeqness.data.database

import androidx.room.TypeConverter
import com.dezdeqness.data.model.db.StatusLocal
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType


class StatusConverter {

    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(List::class.java, StatusLocal::class.java)
    private val jsonAdapter: JsonAdapter<List<StatusLocal>> = moshi.adapter(listMyData)

    @TypeConverter
    fun listMyModelToJsonStr(listMyModel: List<StatusLocal>): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListMyModel(jsonStr: String): List<StatusLocal> {
        return jsonAdapter.fromJson(jsonStr) ?: listOf()
    }
}
