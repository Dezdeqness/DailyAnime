package com.dezdeqness.contract.anime.usecases

import com.dezdeqness.contract.anime.model.AnimeDetailsFullEntity

interface GetAnimeDetailsUseCase {

    suspend operator fun invoke(id: Long): Result<AnimeDetailsFullEntity>
}
