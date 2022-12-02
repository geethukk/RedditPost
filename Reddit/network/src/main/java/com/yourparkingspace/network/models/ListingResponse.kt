package com.yourparkingspace.network.models

data class ListingResponse<T>(
    val children: List<RedditResponse<T>>,
    val after: String?,
    val before: String?
)

internal fun <I, O> ListingResponse<I>.map(transform: (RedditResponse<I>) -> O?): ListingResponse<O> {
    return ListingResponse(
        children.map { response -> response.map { transform(response) } },
        after,
        before
    )
}
