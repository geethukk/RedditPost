package com.yourparkingspace.data.repositories

import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.common_utils.map
import com.yourparkingspace.data.mappers.toDomain
import com.yourparkingspace.domain.model.CommentItem
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.domain.repository.ICommentRepository
import com.yourparkingspace.network.endpoints.PostsEndpoint
import com.yourparkingspace.network.models.CommentDTO
import com.yourparkingspace.network.models.MoreDTO
import javax.inject.Inject

internal class CommentRepository @Inject constructor(
    private val endpoint: PostsEndpoint,
    private val userProfileImageCache: UserProfileImageCache
) : ICommentRepository {

    override suspend fun getComments(post: Post): AppResult<List<CommentItem>> {
        val result = endpoint.getComments(post.subreddit, post.id)
        return result.map { listing ->
            listing.children.mapNotNull {
                when (val data = it.data) {
                    is CommentDTO -> data.toDomain(userProfileImageCache)
                    is MoreDTO -> data.toDomain(userProfileImageCache)
                    else -> null
                }
            }
        }
    }
}
