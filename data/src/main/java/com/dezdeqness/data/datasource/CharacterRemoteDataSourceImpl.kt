package com.dezdeqness.data.datasource

import com.dezdeqness.data.CharacterApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.CharacterMapper
import com.dezdeqness.domain.model.CharacterDetailsEntity
import dagger.Lazy
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val apiService: Lazy<CharacterApiService>,
    private val characterMapper: CharacterMapper,
) : CharacterRemoteDataSource, BaseDataSource() {

    override fun getCharacterDetailsById(id: Long): Result<CharacterDetailsEntity> =
        tryWithCatch {
            val response = apiService.get().getCharacterDetails(id).execute()

            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                val screenshots = characterMapper.fromResponse(responseBody)
                Result.success(screenshots)
            } else {
                throw response.createApiException()
            }
        }

}
