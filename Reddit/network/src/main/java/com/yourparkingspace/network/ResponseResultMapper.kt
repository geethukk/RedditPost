package com.yourparkingspace.network

import com.yourparkingspace.common_utils.AppError
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.network.models.RedditResponse
import java.lang.IllegalStateException

internal fun <T> AppResult<RedditResponse<T>>.mapToDataResult(): AppResult<T> {
    return when (this) {
        is AppResult.Error -> AppResult.Error(error)
        is AppResult.Success -> {
            val responseData = data.data
            return if (responseData == null)
                AppResult.Error(AppError.NotFound)
            else
                AppResult.success(responseData)
        }
        else -> throw IllegalStateException()
    }
}