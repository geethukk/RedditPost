package com.yourparkingspace.domain.model

data class PostWithComments(
    val post: Post,
    val comments: List<CommentItem>
)
