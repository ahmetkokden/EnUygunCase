package com.example.enuyguncase.data.base

data class NetworkResult<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val errorCode: Int? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): NetworkResult<T> =
            NetworkResult(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(error: String?, statusCode: Int): NetworkResult<T> =
            NetworkResult(status = Status.ERROR, data = null, message = error, statusCode)

        fun <T> loading(data: T? = null): NetworkResult<T> =
            NetworkResult(status = Status.LOADING, data = data, message = null)

    }

}