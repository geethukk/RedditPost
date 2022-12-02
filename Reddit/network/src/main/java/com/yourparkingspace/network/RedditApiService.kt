package com.yourparkingspace.network

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.yourparkingspace.network.models.ListingResponse
import com.yourparkingspace.network.models.PostDTO
import com.yourparkingspace.network.models.RedditResponse
import com.yourparkingspace.network.models.SubredditAboutDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RedditApiService {

    // https://www.reddit.com/dev/api/#GET_{sort}
    @GET("/r/{subreddit}{sort-mode}/.json")
    suspend fun getPosts1(
        @Path("subreddit") subreddit: String,
        @Path("sort-mode") sortMode: String,
        @Query("after") after: String? = null
    ): Response<RedditResponse<ListingResponse<PostDTO>>>

    // https://www.reddit.com/dev/api/#GET_r_{subreddit}_about
    @GET("/r/{name}/about/.json")
    suspend fun getSubredditInfo(
        @Path("name") name: String,
        @Query("limit") limit: Int = 10
    ): Response<RedditResponse<SubredditAboutDTO>>

    // https://www.reddit.com/dev/api/#POST_api_vote
    @POST("/api/vote")
    suspend fun vote(
        @Query("id") thingId: String,
        @Query("vote_direction") vote: Int
    ): Response<JsonObject>

    // the first element inside RedditResponse is actually the post
    @GET("/r/{subreddit}/comments/{postId}/.json")
    suspend fun getPostWithComments(
        @Path("subreddit") subreddit: String,
        @Path("postId") postId: String,
    ) : Response<List<JsonElement>>
    companion object {
        const val BASE_URL = "https://www.reddit.com"
    }
}


