package com.yourparkingspace.network.models

import com.google.gson.annotations.SerializedName

data class RedditResponse<T>(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("data")
    val data: T?
)

internal fun <I, O> RedditResponse<I>.map(transform: (I?) -> O?): RedditResponse<O> {
    return RedditResponse(
        kind,
        transform(data)
    )
}