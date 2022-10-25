package com.dezdeqness.domain.model

open class AnimeBriefEntity(
    open val id: Long,
    open val name: String,
    open val russian: String,
    open val images: Map<String, String>,
    open val url: String,
    open val kind: AnimeKind,
    open val score: Float,
    open val status: AnimeStatus,
    open val episodes: Int,
    open val episodesAired: Int,
    open val airedOn: String,
    open val releasedOn: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimeBriefEntity) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (russian != other.russian) return false
        if (images != other.images) return false
        if (url != other.url) return false
        if (kind != other.kind) return false
        if (score != other.score) return false
        if (status != other.status) return false
        if (episodes != other.episodes) return false
        if (episodesAired != other.episodesAired) return false
        if (airedOn != other.airedOn) return false
        if (releasedOn != other.releasedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + russian.hashCode()
        result = 31 * result + images.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + kind.hashCode()
        result = 31 * result + score.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + episodes
        result = 31 * result + episodesAired
        result = 31 * result + airedOn.hashCode()
        result = 31 * result + releasedOn.hashCode()
        return result
    }

    override fun toString() =
        "AnimeBriefEntity(id=$id, name='$name', russian='$russian', images=$images, url='$url', kind=$kind, score=$score, status=$status, episodes=$episodes, episodesAired=$episodesAired, airedOn='$airedOn', releasedOn='$releasedOn')"

}
