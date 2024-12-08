package com.dezdeqness.data.provider

class HomeGenresProvider {

    fun getHomeSectionGenresIds() =
        listOf(
            GENRE_ID_ADVENTURE,
            GENRE_ID_SHOUNEN,
            GENRE_ID_ROMANTIC,
        )

    companion object {
        private const val GENRE_ID_ADVENTURE = "2"
        private const val GENRE_ID_ROMANTIC = "22"
        private const val GENRE_ID_SHOUNEN = "27"
    }
}
