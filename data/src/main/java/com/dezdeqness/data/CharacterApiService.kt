package com.dezdeqness.data

import com.dezdeqness.data.model.CharacterDetailsRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApiService {
    @GET("characters/{id}")
    fun getCharacterDetails(@Path(value = "id") id: Long): Call<CharacterDetailsRemote>
}
