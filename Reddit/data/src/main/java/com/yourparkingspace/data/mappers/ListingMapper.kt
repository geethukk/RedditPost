package com.yourparkingspace.data.mappers

import com.yourparkingspace.network.models.ListingResponse
import com.yourparkingspace.domain.model.Listing

internal fun <T,R> ListingResponse<T>.toDomainListing(mapper: (T) -> R) : Listing<R> {

    return Listing(
        this.children.mapNotNull { it.data?.let { data -> mapper(data) } }.toMutableList(),
        this.after,
        this.before
    )
}

