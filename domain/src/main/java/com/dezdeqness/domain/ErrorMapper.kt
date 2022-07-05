package com.dezdeqness.domain

interface ErrorMapper {

    fun map(exception: Throwable): ErrorEntity

}
