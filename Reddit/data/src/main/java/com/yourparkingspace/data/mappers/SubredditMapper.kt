package com.yourparkingspace.data.mappers

import com.yourparkingspace.database.entity.SubredditEntity
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.network.models.SubredditAboutDTO

internal fun SubredditAboutDTO.toDomainSubreddit(): Subreddit? {
    return Subreddit(
        name = name ?: "",
        description = description.orEmpty(),
        subscriptions = subs ?: 0,
        activeUsers = activeUsers ?: 0,
        icon = image.url() ?: icon?.url(),
        backgroundImage = backgroundImage?.url() ?: bannerImage?.url(),
        keyColor = keyColor?.takeIf { it.isNotEmpty() }
    )
}

internal fun Subreddit.mapToEntity() : SubredditEntity {
    return SubredditEntity(
        name = name,
        description = description,
        subscriptions = subscriptions,
        activeUsers = activeUsers,
        backgroundImage = backgroundImage,
        keyColor = keyColor,
        iconUrl = icon,
        dateModified = System.currentTimeMillis(),
    )
}

internal fun SubredditEntity.mapToDomain() : Subreddit {
    return Subreddit(
        name = name,
        description = description,
        subscriptions = subscriptions,
        activeUsers = activeUsers,
        backgroundImage = backgroundImage,
        keyColor = keyColor,
        icon = iconUrl
    )
}

private fun String?.url(): String? = takeIf { this?.isNotEmpty() == true }?.substringBefore("?")
