package com.dezdeqness.data.provider

import android.content.res.AssetManager
import com.dezdeqness.data.mapper.GenreMapper
import com.dezdeqness.data.model.GenreRemote
import com.dezdeqness.domain.model.GenreEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.buffer
import okio.source

class ConfigurationProvider(
    private val assetManager: AssetManager,
    private val genreMapper: GenreMapper,
    private val moshi: Moshi,
) {

    // TODO: Make remote
    fun getListGenre(): List<GenreEntity> {
        val inputStream = assetManager.open(FILENAME_GENRE_JSON)
        val type = Types.newParameterizedType(List::class.java, GenreRemote::class.java)
        val adapter = moshi.adapter<List<GenreRemote>>(type)
        val list = adapter.fromJson(inputStream.source().buffer()).orEmpty()
        return list.map { item -> genreMapper.fromResponse(item) }
    }

    companion object {
        private const val FILENAME_GENRE_JSON = "genre.json"
    }

}
