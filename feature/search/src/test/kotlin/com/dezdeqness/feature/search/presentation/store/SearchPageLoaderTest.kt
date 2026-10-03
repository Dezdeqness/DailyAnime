package com.dezdeqness.feature.search.presentation.store

import com.dezdeqness.architecture.store.paging.PageResult
import com.dezdeqness.contract.anime.model.AnimeBriefEntity
import com.dezdeqness.contract.anime.model.AnimeSearchParams
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase
import com.dezdeqness.contract.anime.usecases.GetAnimeListUseCase.AnimeListState
import com.dezdeqness.feature.search.presentation.AnimeUiMapper
import com.dezdeqness.feature.search.presentation.models.AnimeUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchPageLoaderTest {

    private val getAnimeListUseCase = mockk<GetAnimeListUseCase>()
    private val animeUiMapper = mockk<AnimeUiMapper>()

    private val params = SearchPageParams(page = 1, params = AnimeSearchParams(text = "naruto"))
    private val entities = listOf(mockk<AnimeBriefEntity>())
    private val items = listOf(AnimeUiModel(id = 1L, title = "Naruto", kind = "TV", logoUrl = ""))

    private lateinit var loader: SearchPageLoader

    @Before
    fun setup() {
        every { animeUiMapper.map(entities) } returns items

        loader = SearchPageLoader(getAnimeListUseCase, animeUiMapper)
    }

    @Test
    fun `WHEN more pages exist SHOULD return mapped items and the next page params`() = runTest {
        coEvery { getAnimeListUseCase.invoke(params.page, params.params) } returns Result.success(AnimeListState(list = entities, hasNextPage = true, currentPage = 2))

        val result = loader.load(params)

        assertEquals(PageResult.Success(items = items, nextParams = params.copy(page = 2)), result)
    }

    @Test
    fun `WHEN it is the last page SHOULD return no next params`() = runTest {
        coEvery { getAnimeListUseCase.invoke(params.page, params.params) } returns Result.success(AnimeListState(list = entities, hasNextPage = false, currentPage = 2))

        val result = loader.load(params)

        assertEquals(PageResult.Success(items = items, nextParams = null), result)
    }

    @Test
    fun `WHEN network fails SHOULD return Network error`() = runTest {
        coEvery { getAnimeListUseCase.invoke(params.page, params.params) } returns Result.failure(IOException())

        assertEquals(PageResult.Failure(SearchError.Network), loader.load(params))
    }

    @Test
    fun `WHEN something else fails SHOULD return Unknown error with the cause`() = runTest {
        val cause = IllegalStateException("boom")
        coEvery { getAnimeListUseCase.invoke(params.page, params.params) } returns Result.failure(cause)

        assertEquals(PageResult.Failure(SearchError.Unknown(cause)), loader.load(params))
    }

}
