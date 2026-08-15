package com.dezdeqness.contract.person.model

import com.dezdeqness.contract.favourite.model.FavouriteKind

fun Set<PersonRole>.toPrimaryFavouriteKind(): FavouriteKind = when {
    PersonRole.SEYU in this -> FavouriteKind.SEYU
    PersonRole.MANGAKA in this -> FavouriteKind.MANGAKA
    PersonRole.PRODUCER in this -> FavouriteKind.PRODUCER
    else -> FavouriteKind.PERSON
}
