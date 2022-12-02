package com.yourparkingspace.domain.repository

import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.*

interface IPostRepo {

    suspend fun getPostsListing(
        subreddit: String,
        sortMode: PostSortMode,
        pageKey: String? = null,
        forceRefresh: Boolean = false
    ): AppResult<Listing<Post>>

    suspend fun vote(post: Post, voteType: VoteType) : AppResult<Unit>

    fun saveSortMode(subredditName: String, sortMode: PostSortMode)

    fun getSortMode(subredditName: String): PostSortMode

    fun invalidateCache()

    suspend fun getPost(id: String, subredditName: String, forceRefresh: Boolean = false): Post?

    suspend fun getPostWithComments(id: String, subredditName: String) : AppResult<PostWithComments>

    val pageSize: Int
}