package com.dezdeqness.data.database

import androidx.room.TypeConverter
import com.dezdeqness.data.model.db.StatsLocal
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class StatsConverter {

    private val moshi = Moshi.Builder().build()
    private val listMyData : ParameterizedType = Types.newParameterizedType(List::class.java, StatsLocal::class.java)
    private val jsonAdapter: JsonAdapter<List<StatsLocal>> = moshi.adapter(listMyData)

    @TypeConverter
    fun listMyModelToJsonStr(listMyModel: List<StatsLocal>): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListMyModel(jsonStr: String): List<StatsLocal> {
        return jsonAdapter.fromJson(jsonStr) ?: listOf()
    }

}