package com.yourparkingspace.domain.model

data class Subreddit(
    val name: String,
    val description: String,
    val subscriptions: Int,
    val activeUsers: Int,
    val icon: String?,
    val backgroundImage: String?,
    val keyColor: String?,
    var subscriptionState: SubscriptionState = SubscriptionState.NotSubscribed
) {
    val isFavorite get() = (subscriptionState as? SubscriptionState.Subscribed)?.isFavorite == true
    val isSubscribed get() = subscriptionState is SubscriptionState.Subscribed
    val prefixedName get() = "r/$name"
}

sealed class SubscriptionState {

    object NotSubscribed : SubscriptionState()

    data class Subscribed(
        var isFavorite: Boolean,
    ) : SubscriptionState()
}