package com.dezdeqness.data.core

class ApiException(
    val code: Int,
    override val message: String,
) : Throwable(message)

class GraphqlException(
    val operationName: String,
    override val message: String,
) : Throwable(message)
