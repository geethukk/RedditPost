package com.yourparkingspace.reddit.ui.base

import com.yourparkingspace.common_utils.AppError

interface UiEvent

object Events {
    class ShowToast(val message: String) : UiEvent
    class ShowLongToast(val message: String) : UiEvent
    class ShowError(val error: AppError) : UiEvent
    class OpenUrl(val url: String) : UiEvent
}
