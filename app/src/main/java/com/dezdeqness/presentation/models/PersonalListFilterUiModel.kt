package com.dezdeqness.presentation.models

import com.dezdeqness.domain.model.Sort

data class PersonalListFilterUiModel(
    val sort: Sort,
    val isAscending: Boolean,
) : AdapterItem() {

    override fun id() = "PersonalListFilterUiModel"

    override fun payload(other: Any): Payloadable {
        if (other !is PersonalListFilterUiModel) return super.payload(other)

        if (other.sort != sort) {
            return PersonalListFilterPayload(sort = sort)
        }

        if (other.isAscending != isAscending) {
            return PersonalListFilterPayload(isAscending = isAscending)
        }

        return super.payload(other)
    }

    data class PersonalListFilterPayload(
        val sort: Sort? = null,
        val isAscending: Boolean? = null,
    ) : Payloadable

}
