package com.dezdeqness.feature.details.anime.presentation.store

import com.dezdeqness.feature.details.common.presentation.store.BaseDetailsReducer
import com.dezdeqness.feature.details.common.presentation.store.DetailsStatus

val animeDetailsReducer = object : BaseDetailsReducer<
        AnimeDetailsNamespace.Event,
        AnimeDetailsNamespace.State,
        AnimeDetailsNamespace.Effect,
        AnimeDetailsNamespace.Command,
        >(
    wrapCommand = AnimeDetailsNamespace.Command::Base,
    wrapEffect = AnimeDetailsNamespace.Effect::Base,
) {
    override fun Result.reduce(event: AnimeDetailsNamespace.Event) {
        when (event) {
            is AnimeDetailsNamespace.Event.Base -> handleBaseDetailsEvent(
                event = event.event,
                onInitialLoad = { id ->
                    AnimeDetailsNamespace.State(
                        id = id,
                        status = DetailsStatus.Loading,
                    )
                },
                onLoading = { state.copy(status = DetailsStatus.Loading) },
                onError = { state.copy(status = DetailsStatus.Error) },
            )

            is AnimeDetailsNamespace.Event.OnDetailsLoaded -> {
                val entity = event.details.animeDetailsEntity
                state {
                    state.copy(
                        status = DetailsStatus.Loaded,
                        details = event.details,
                        sections = event.sections,
                        isAuthorized = event.isAuthorized,
                        title = entity.russian,
                        shareUrl = entity.url,
                    )
                }
            }

            is AnimeDetailsNamespace.Event.EditRateClicked -> {
                val entity = state.details?.animeDetailsEntity ?: return
                state {
                    state.copy(
                        editRateSheet = EditRateSheetState.Visible(
                            userRateId = entity.userRate?.id ?: -1L,
                            title = entity.russian,
                        ),
                    )
                }
            }

            is AnimeDetailsNamespace.Event.EditRateClosed -> {
                state { state.copy(editRateSheet = EditRateSheetState.None) }
            }

            is AnimeDetailsNamespace.Event.SaveUserRate -> {
                state { state.copy(editRateSheet = EditRateSheetState.None) }
                commands {
                    +AnimeDetailsNamespace.Command.CreateOrUpdateUserRate(
                        animeId = state.id,
                        model = event.model,
                    )
                }
            }

            is AnimeDetailsNamespace.Event.OnUserRateSaved -> {
                val current = state.details ?: return
                state {
                    state.copy(
                        details = current.copy(
                            animeDetailsEntity = current.animeDetailsEntity.copy(
                                userRate = event.rate,
                            ),
                        ),
                    )
                }
                effects {
                    if (event.isCreate) +AnimeDetailsNamespace.Effect.EditRateCreated(event.rate)
                    else +AnimeDetailsNamespace.Effect.EditRateUpdated(event.rate)
                }
            }

            is AnimeDetailsNamespace.Event.OnUserRateSaveError -> {
                effects { +AnimeDetailsNamespace.Effect.EditRateError }
            }

            is AnimeDetailsNamespace.Event.StatsClicked -> {
                val entity = state.details?.animeDetailsEntity ?: return
                effects {
                    +AnimeDetailsNamespace.Effect.NavigateToStats(
                        scores = entity.scoresStats,
                        statuses = entity.statusesStats,
                    )
                }
            }

            is AnimeDetailsNamespace.Event.SimilarClicked -> {
                val animeId = state.details?.animeDetailsEntity?.id ?: return
                effects { +AnimeDetailsNamespace.Effect.NavigateToSimilar(animeId) }
            }

            is AnimeDetailsNamespace.Event.ChronologyClicked -> {
                val animeId = state.details?.animeDetailsEntity?.id ?: return
                effects { +AnimeDetailsNamespace.Effect.NavigateToChronology(animeId) }
            }

            is AnimeDetailsNamespace.Event.RelatedClicked -> {
                effects { +AnimeDetailsNamespace.Effect.NavigateToAnime(event.animeId) }
            }

            is AnimeDetailsNamespace.Event.CharacterClicked -> {
                effects { +AnimeDetailsNamespace.Effect.NavigateToCharacter(event.characterId) }
            }

            is AnimeDetailsNamespace.Event.ScreenshotClicked -> {
                val screenshots = state.details?.screenshots ?: return
                val index = screenshots.indexOfFirst { event.previewUrl.contains(it.preview) }
                if (index < 0) return
                effects {
                    +AnimeDetailsNamespace.Effect.NavigateToScreenshotViewer(
                        index = index,
                        urls = screenshots.map { it.original },
                    )
                }
            }

            is AnimeDetailsNamespace.Event.VideoClicked -> {
                effects { +AnimeDetailsNamespace.Effect.OpenVideo(event.url) }
            }
        }
    }
}
