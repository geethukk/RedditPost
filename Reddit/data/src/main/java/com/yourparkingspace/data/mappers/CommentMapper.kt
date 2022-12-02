package com.yourparkingspace.data.mappers

import com.yourparkingspace.data.repositories.UserProfileImageCache
import com.yourparkingspace.domain.model.*
import com.yourparkingspace.network.models.CommentDTO
import com.yourparkingspace.network.models.CommentResponseDTO
import com.yourparkingspace.network.models.MoreDTO

internal fun CommentResponseDTO.toDomain(userProfileImageCache: UserProfileImageCache): CommentItem {
    return when (this) {
        is MoreDTO -> toDomain()
        is CommentDTO -> toDomain(userProfileImageCache)
    }
}

internal fun CommentDTO.toDomain(userProfileImageCache: UserProfileImageCache): Comment {

    val children = childComments?.children ?: listOf()

    return Comment(
        id = id,
        authorId = authorId.orEmpty(),
        authorName = authorName.orEmpty(),
        text = body.orEmpty(),
        awards = Awards(0, listOf()),
        date = dateCreated,
        score = score,
        itemDepth = depth,
        myVote = VoteType.NONE,
        isPostAuthor = isPostAuthor,
        replies = children.mapNotNull { it.data?.toDomain(userProfileImageCache) },
        authorImage = userProfileImageCache.getImageForUserId(authorId.toString()),
    )
}

private fun MoreDTO.toDomain(): MoreComments {
    return MoreComments(count, depth)
}