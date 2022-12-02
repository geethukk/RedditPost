package com.yourparkingspace.domain.usecase

import com.yourparkingspace.common_utils.AppError
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.domain.model.VoteType
import com.yourparkingspace.domain.repository.IAuthenticator
import com.yourparkingspace.domain.repository.IPostRepo
import javax.inject.Inject

class VoteUseCase @Inject constructor(
    private val userRepo: IAuthenticator,
    private val postRepo: IPostRepo
) {
    suspend fun vote(
        post: Post,
        isUpvote: Boolean
    ): AppResult<Post> {

        if (!userRepo.isLoggedIn()) {
            return AppResult.failed(AppError.Unauthenticated)
        }

        val newVote = if (isUpvote) VoteType.UPVOTE else VoteType.DOWNVOTE
        val modifier = if (isUpvote) 1 else -1

        when (post.vote) {
            VoteType.NONE -> {
                post.vote = newVote
                post.score += modifier
            }
            newVote -> {
                post.vote = VoteType.NONE
                post.score -= modifier
            }
            else -> {
                post.vote = newVote
                post.score += modifier * 2
            }
        }

       return when (val result = postRepo.vote(post, post.vote)) {
           is AppResult.Error -> result
           is AppResult.Success -> AppResult.success(post)
       }
    }
}