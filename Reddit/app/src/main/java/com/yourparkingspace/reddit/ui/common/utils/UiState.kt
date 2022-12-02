package com.yourparkingspace.reddit.ui.common.utils

import com.yourparkingspace.common_utils.AppError
import java.util.*

sealed class UiState<out T> {

    data class Data<out T>(
        val data: T,
        val nextPageLoading: PaginationLoadingState = PaginationLoadingState.Completed,
        val isRefreshing: Boolean = false,
        private val uuid: UUID = UUID.randomUUID()
    ) : UiState<T>()

    data class Error(val error: AppError) : UiState<Nothing>()
    data class Loading<out T>(val text: String? = null) : UiState<T>()

    fun copy(): UiState<T> {
        return when (this) {
            is Data -> Data(data, nextPageLoading, isRefreshing)
            is Error -> Error(error)
            is Loading -> Loading()
        }
    }
}

sealed class PaginationLoadingState {
    object Completed : PaginationLoadingState()
    object Loading : PaginationLoadingState()
    object Error : PaginationLoadingState()
}