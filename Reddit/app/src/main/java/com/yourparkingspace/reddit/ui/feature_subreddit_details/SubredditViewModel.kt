package com.yourparkingspace.reddit.ui.feature_subreddit_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.domain.repository.ISubredditRepository
import com.yourparkingspace.reddit.ui.base.BaseViewModel
import com.yourparkingspace.reddit.ui.base.Events
import com.yourparkingspace.reddit.ui.common.utils.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditViewModel @Inject constructor(
    private val repo: ISubredditRepository
) : BaseViewModel() {

    private val sub: MutableLiveData<Subreddit> = MutableLiveData()
    val subreddit = sub.immutable()

    private val _showLeaveDialog = MutableStateFlow(false)
    val showLeaveDialog = _showLeaveDialog.asStateFlow()

    fun setSubreddit(name: String) {
        if (sub.value == null) {
            viewModelScope.launch {
                repo.getSubredditFlow(name).collect { s ->
                    if (s != null)
                        sub.postValue(s)
                }
            }
        }
    }

    fun confirmLeave() {
        viewModelScope.launch {
            if (sub.value?.isSubscribed == true) {
                _showLeaveDialog.emit(false)
                changeSubscriptionState(false)
            }
        }
    }

    fun cancelLeaving() {
        viewModelScope.launch {
            _showLeaveDialog.emit(false)
        }
    }

    fun changeSubscriptionState(askForConfirmation: Boolean = true) {
        viewModelScope.launch {

            if (askForConfirmation && sub.value?.isSubscribed == true && !showLeaveDialog.value) {
                _showLeaveDialog.emit(true)
                return@launch
            }

            delay(100)
            sub.value?.let {
                when (val result = repo.changeSubscriptionState(it)) {
                    is AppResult.Error -> dispatchEvent(Events.ShowError(result.error))
                    is AppResult.Success -> sub.postValue(result.data)
                }
            }
        }
    }
}