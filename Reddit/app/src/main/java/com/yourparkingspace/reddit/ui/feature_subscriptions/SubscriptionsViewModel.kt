package com.yourparkingspace.reddit.ui.feature_subscriptions

import androidx.lifecycle.viewModelScope
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.domain.model.SubscriptionState
import com.yourparkingspace.reddit.ui.common.utils.UiState
import com.yourparkingspace.domain.repository.ISubredditRepository
import com.yourparkingspace.reddit.ui.base.BaseViewModel
import com.yourparkingspace.reddit.ui.base.Events
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val subredditRepository: ISubredditRepository
) : BaseViewModel() {

    private var subscriptions = SubscriptionsState()

    private val _subreddits = MutableStateFlow<UiState<SubscriptionsState>>(UiState.Loading())
    val subreddits: StateFlow<UiState<SubscriptionsState>> = _subreddits

    private var key = 0

    init {
        viewModelScope.launch {
            combine(
                subredditRepository.observeRecentlyVisited(),
                subredditRepository.getSubscribedSubreddits()
            ) { recent, followed ->
                SubscriptionsState(
                    0,
                    followed.filter { sub -> sub.isFavorite }.toMutableList(),
                    followed.toMutableList(),
                    recent
                )
            }.collect {
                subscriptions = it
                _subreddits.emit(UiState.Data(it))
            }
        }
    }

    fun deleteFromVisited(subreddit: Subreddit) {
        viewModelScope.launch {
            subredditRepository.deleteFromVisited(subreddit.name)
        }
    }

    fun addToVisited(subreddit: Subreddit) {
        viewModelScope.launch {
            subredditRepository.visited(subreddit.name)
        }
    }

    fun retry() {
        // TODO implement once data is loaded from network instead of db
    }

    fun changeFavoriteState(subreddit: Subreddit) {
        viewModelScope.launch {
            delay(250)
            val subState = subreddit.subscriptionState

            if (subState is SubscriptionState.Subscribed) {
                val newFavoriteState = !subreddit.isFavorite
                val result = subredditRepository.setFavorite(subreddit, newFavoriteState)
                if (result is AppResult.Error) {
                    dispatchEvent(Events.ShowError(result.error))
                    return@launch
                }

                if (subreddit.isFavorite) {
                    subscriptions.favorites.remove(subreddit)
                } else {
                    subscriptions.favorites.add(subreddit)
                }

                subState.isFavorite = newFavoriteState
                subscriptions = subscriptions.copy(key = ++key)
                _subreddits.emit(UiState.Data(subscriptions))
            }
        }
    }

}