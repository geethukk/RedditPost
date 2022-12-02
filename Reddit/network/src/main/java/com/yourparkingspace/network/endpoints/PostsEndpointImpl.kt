package com.yourparkingspace.network.endpoints

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.common_utils.map
import com.yourparkingspace.network.RedditApiService
import com.yourparkingspace.network.fromJson
import com.yourparkingspace.network.mapToDataResult
import com.yourparkingspace.network.models.*
import com.yourparkingspace.network.safeCall
import javax.inject.Inject
import javax.inject.Singleton

interface PostsEndpoint {
    suspend fun getPosts(
        subreddit: String,
        sortMode: String,
        after: String? = null,
    ): AppResult<ListingResponse<PostDTO>>

    suspend fun vote(
        postId: String,
        vote: Int
    ): AppResult<Unit>

    suspend fun getComments(
        subreddit: String,
        postId: String,
    ): AppResult<ListingResponse<CommentResponseDTO>>

    suspend fun getPostWithComments(
        subreddit: String,
        postId: String,
    ): AppResult<PostWithCommentsResponse>
}

@Singleton
internal class PostsEndpointImpl @Inject constructor(
    private val service: RedditApiService,
) : PostsEndpoint {

    private val gson = Gson()

    override suspend fun getPosts(
        subreddit: String,
        sortMode: String,
        after: String?
    ): AppResult<ListingResponse<PostDTO>> {
        return safeCall { service.getPosts1(subreddit, sortMode, after) }.mapToDataResult()
    }

    override suspend fun vote(
        postId: String,
        vote: Int
    ): AppResult<Unit> {
        return safeCall { service.vote(postId, vote) }.map { }
    }

    override suspend fun getComments(
        subreddit: String,
        postId: String,
    ): AppResult<ListingResponse<CommentResponseDTO>> {
        val result = safeCall { service.getPostWithComments(subreddit, postId) }

        // index 0 - post
        // index 1 - comments
        return result.map { data ->
            val listing = data.getOrNull(1)?.let {
                parseCommentsResponse(it)
            }
            listing?.convertReplies()
            listing
        }
    }

    private fun parseCommentsResponse(json: JsonElement) : ListingResponse<CommentResponseDTO>? {
        val listingResponse =
            gson.fromJson<RedditResponse<ListingResponse<JsonElement>>>(json)?.data
        return listingResponse?.map { res ->
            if (res.data == null) {
                return@map null
            }
            when (res.kind) {
                ItemKind.COMMENT.id -> gson.fromJson<CommentDTO>(res.data)
                ItemKind.MORE.id -> gson.fromJson<MoreDTO>(res.data)
                else -> null
            }
        }
    }

    override suspend fun getPostWithComments(
        subreddit: String,
        postId: String
    ): AppResult<PostWithCommentsResponse> {

        val result = safeCall { service.getPostWithComments(subreddit, postId) }

        return result.map { data ->
            val postDto: PostDTO? = data.getOrNull(0)?.let {
                val response = gson.fromJson<RedditResponse<ListingResponse<PostDTO>>>(it)
                response?.data?.children?.first()?.data
            }
            val listing = data.getOrNull(1)?.let {
                parseCommentsResponse(it)
            }
            listing?.convertReplies()

            if (postDto == null) {
                null
            } else {
                PostWithCommentsResponse(postDto, listing)
            }
        }
    }

    private fun ListingResponse<CommentResponseDTO>.convertReplies() {
        children.forEach {
            val dto = it.data
            if (dto is CommentDTO) {
                dto?.let {
                    val replies = gson.fromJson<RedditResponse<ListingResponse<JsonElement>>>(dto.repliesJson)
                    val items = replies?.map { res ->
                        res?.map {
                            if (it.data == null) {
                                return@map null
                            }
                            when (it.kind) {
                                ItemKind.COMMENT.id -> gson.fromJson<CommentDTO>(it.data)
                                ItemKind.MORE.id -> gson.fromJson<MoreDTO>(it.data)
                                else -> null
                            }
                        }
                    }
                    dto.childComments = items?.data
                    dto.childComments?.convertReplies()
                }
            }
        }
    }
}