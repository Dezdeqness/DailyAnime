package com.dezdeqness.contract.favourite.model

fun FavouriteLinkedType.matchingCacheTypes(): Set<FavouriteType> = when (this) {
    FavouriteLinkedType.ANIME -> setOf(FavouriteType.ANIME)
    FavouriteLinkedType.MANGA -> setOf(FavouriteType.MANGA)
    FavouriteLinkedType.RANOBE -> setOf(FavouriteType.RANOBE)
    FavouriteLinkedType.CHARACTER -> setOf(FavouriteType.CHARACTER)
    FavouriteLinkedType.PERSON -> setOf(
        FavouriteType.PERSON,
        FavouriteType.MANGAKA,
        FavouriteType.SEYU,
        FavouriteType.PRODUCER,
    )
}
