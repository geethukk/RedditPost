package com.yourparkingspace.reddit.ui.feature_posts.post_list

import android.util.Log
import com.yourparkingspace.common_utils.AppError
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.Listing
import com.yourparkingspace.reddit.ui.common.utils.PaginationLoadingState
import com.yourparkingspace.reddit.ui.common.utils.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class PagedDataSource<Data>() {

    private val data = mutableListOf<Data>()
    private val listings = mutableListOf<Listing<Data>>()
    private var paginationLoadingState: PaginationLoadingState = PaginationLoadingState.Completed

    private val _state: MutableStateFlow<UiState<List<Data>>> = MutableStateFlow(UiState.Loading())
    val state = _state.asStateFlow()

    private val _errors = MutableSharedFlow<AppError>()
    val errors = _errors.asSharedFlow()

    abstract val pageSize: Int

    suspend fun refresh() {
        when (val result = loadDataForKey(null, true)) {
            is AppResult.Error -> {
                _errors.emit(result.error)
            }
            is AppResult.Success -> {
                data.clear()
                listings.clear()
                listings.add(result.data)
                data.addAll(result.data.items)
                _state.emit(UiState.Data(data))
            }
        }
    }

    suspend fun loadNextPage() {

        Log.d("pagination", "loadNextPage")

        if (paginationLoadingState != PaginationLoadingState.Completed) {
            return
        }

        val nextKey = listings.lastOrNull()?.after
        val isFirstPage = nextKey == null

        if (data.isNotEmpty() && data.size < pageSize) {
            return
        }

        if (isFirstPage) {
            _state.emit(UiState.Loading())
        } else {
            paginationLoadingState = PaginationLoadingState.Loading
            _state.emit(UiState.Data(data, paginationLoadingState))
        }

        when (val result = loadDataForKey(nextKey, false)) {
            is AppResult.Success -> {
                listings.add(result.data)
                data.addAll(result.data.items)
                _state.emit(UiState.Data(data))
                paginationLoadingState = PaginationLoadingState.Completed
            }
            is AppResult.Error -> {
                if (isFirstPage) {
                    _state.emit(UiState.Error(result.error))
                } else {
                    paginationLoadingState = PaginationLoadingState.Error
                    _state.emit(
                        UiState.Data(
                            data,
                            PaginationLoadingState.Error
                        )
                    )
                }
            }
        }
    }

    abstract suspend fun loadDataForKey(key: String?, refresh: Boolean): AppResult<Listing<Data>>

    suspend fun retry() {
        paginationLoadingState = PaginationLoadingState.Completed
        loadNextPage()
    }
}