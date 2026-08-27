package com.dezdeqness.data.core

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation

fun <T : Operation.Data> ApolloResponse<T>.createGraphqlException() = GraphqlException(
    operation.name(),
    errors?.toString()
        ?: exception?.toString()
        ?: "No mappable error",
)
