package com.yourparkingspace.common_utils

sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val error: AppError) : AppResult<Nothing>()

    fun isSuccess() = this is Success
    fun isError() = this is Error

    companion object {
        fun <T> success(data: T) = Success(data)
        fun success() = Success(Unit)
        fun failed(error: AppError) = Error(error)
    }
}

fun <I, O> AppResult<I>.map(
    transform: (I) -> O?,
    //nullValueTransform: AppError = AppError.NotFound
): AppResult<O> {
    return when (this) {
        is AppResult.Error -> this
        is AppResult.Success -> {
            val value = transform(this.data)
            if (value != null)
                AppResult.success(value)
            else
                AppResult.failed(AppError.NotFound )
        }
    }
}
