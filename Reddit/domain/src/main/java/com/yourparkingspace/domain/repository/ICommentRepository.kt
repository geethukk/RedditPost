package com.yourparkingspace.domain.repository

import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.CommentItem
import com.yourparkingspace.domain.model.Post

interface ICommentRepository {

    suspend fun getComments(post: Post): AppResult<List<CommentItem>>
}