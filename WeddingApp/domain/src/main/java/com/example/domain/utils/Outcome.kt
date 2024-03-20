package com.example.domain.utils

sealed class Outcome<out SuccessResultType : Any, out ErrorType : Any> {
    data class Success<out SuccessResultType : Any>(
        val value: SuccessResultType? = null
    ): Outcome<SuccessResultType, Nothing>()

    data class Failure<out ErrorType : Any>(
        val error: ErrorType,
        val message: String? = null,
        val throwable: Throwable? = null,
    ): Outcome<Nothing, ErrorType>()
}