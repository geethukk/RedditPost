package com.yourparkingspace.reddit.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

const val DELAY_NAVIGATION_START: Long = 100L
const val DELAY_NAVIGATION_REENABLE: Long = 250L

abstract class BaseViewModel : ViewModel() {

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)

    val eventFLow: Flow<UiEvent> = _eventChannel.receiveAsFlow()

    private val currentEvents: MutableSet<UiEvent> = mutableSetOf()

    protected fun dispatchEvent(
        event: UiEvent,
        delayBefore: Long = 0,
        delayAfter: Long = 0
    ) = viewModelScope.launch {

        if (currentEvents.contains(event)) {
            return@launch
        }

        currentEvents.add(event)

        if (delayBefore > 0)
            delay(delayBefore)

        _eventChannel.trySend(event)

        if (delayAfter > 0)
            delay(delayAfter)

        currentEvents.remove(event)
    }

    protected fun dispatchNavigationEvent(
        event: UiEvent,
        delayBefore: Long = DELAY_NAVIGATION_START,
        delayAfter: Long = DELAY_NAVIGATION_REENABLE
    ) {
        dispatchEvent(event, delayBefore, delayAfter)
    }

}
