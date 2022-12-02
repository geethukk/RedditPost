package com.yourparkingspace.network.models

data class PostWithCommentsResponse(
    val post: PostDTO,
    val comments: ListingResponse<CommentResponseDTO>?
)