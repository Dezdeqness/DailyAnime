package com.dezdeqness.presentation.models

data class UserRateUiModel(
    val rateId: Long,
    val id: Long,
    val name: String,
    val briefInfo: String,
    val score: String,
    val progress: String,
    val logoUrl: String,
    val overallEpisodes: Int,
    val isAnimeInProgress: Boolean,
) : AdapterItem() {

    override fun id() = (rateId + id).toString()

    override fun payload(other: Any): Payloadable {
        if (other !is UserRateUiModel) return super.payload(other)

        var payload: UserRateUiModelPayload? = null
        if (other.name != name) {
            payload = UserRateUiModelPayload(name = other.name)
        }

        if (other.briefInfo != briefInfo) {
            payload = payload?.copy(briefInfo = other.briefInfo) ?: UserRateUiModelPayload(briefInfo = other.briefInfo)
        }

        if (other.score != score) {
            payload = payload?.copy(score = other.score) ?: UserRateUiModelPayload(score = other.score)
        }

        if (other.progress != progress) {
            payload = payload?.copy(progress = other.progress) ?: UserRateUiModelPayload(progress = other.progress)
        }

        payload?.let {
            return it
        }

        return super.payload(other)
    }

    data class UserRateUiModelPayload(
        val name: String? = null,
        val briefInfo: String? = null,
        val score: String? = null,
        val progress: String? = null,
        val logoUrl: String? = null,
    ) : Payloadable

}
