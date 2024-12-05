package com.dezdeqness.data.core

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GraphqlOperationNameInterceptor @Inject constructor() : ApolloInterceptor {
    override fun <D : Operation.Data> intercept(request: ApolloRequest<D>, chain: ApolloInterceptorChain): Flow<ApolloResponse<D>> {
        return chain.proceed(request.newBuilder()
            .addHttpHeader(APOLLO_OPERATION_NAME, request.operation.name())
            .build()
        )
    }

    companion object {
        private const val APOLLO_OPERATION_NAME = "X-APOLLO-OPERATION-NAME"
    }
}
