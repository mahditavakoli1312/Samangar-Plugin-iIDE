package com.example.common


sealed interface ResultWrapper<out T> {
    data class Success<T>(
        val networkState: NetworkState = NetworkState.SUCCESS,
        val resultData: T
    ) : ResultWrapper<T>

    data class Failure<T>(
        val errorType: NetworkState,
        val message: String = "",
        val messageId: Int? = null,
        val code: Int? = null
    ) : ResultWrapper<T>
}
