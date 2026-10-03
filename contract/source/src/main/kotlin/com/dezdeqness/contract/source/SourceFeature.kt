package com.dezdeqness.contract.source

sealed interface SourceFeature

interface SourceSearch : SourceFeature

interface SourceSearchFilter : SourceFeature

interface SourceAnimeDetails : SourceFeature

interface SourceChronology : SourceFeature

interface SourceSimilar : SourceFeature

interface SourceAnimeStats : SourceFeature

interface SourceCharacterDetails : SourceFeature

interface SourcePersonDetails : SourceFeature

interface SourceCalendar : SourceFeature

interface SourcePersonalList : SourceFeature

interface SourceHistory : SourceFeature

interface SourceFavourites : SourceFeature

interface SourceAchievements : SourceFeature

interface SourceNews : SourceFeature
