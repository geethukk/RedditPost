package com.yourparkingspace.data.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.common_utils.map
import com.yourparkingspace.data.mappers.toDomain
import com.yourparkingspace.network.endpoints.PostsEndpoint
import com.yourparkingspace.data.mappers.toDomainListing
import com.yourparkingspace.data.mappers.toDomainPost
import com.yourparkingspace.domain.model.*
import com.yourparkingspace.domain.repository.IPostRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PostRepo @Inject constructor(
    private val postsEndpoint: PostsEndpoint,
    private val sp: SharedPreferences,
    private val userProfileImageCache: UserProfileImageCache

) : IPostRepo {

    private val cache = PostInMemoryCache()

    override val pageSize: Int
        get() = 10

    override fun invalidateCache() {
        cache.clear()
    }

    override suspend fun getPost(id: String, subredditName: String, forceRefresh: Boolean): Post? {

        if (!forceRefresh) {
            cache.getPost(id)?.let {
                return it
            }
        }

        return when (val result = postsEndpoint.getPostWithComments(subredditName, id)) {
            is AppResult.Error -> null
            is AppResult.Success -> result.data.post.toDomainPost()
        }
    }

    override suspend fun getPostWithComments(id: String, subredditName: String): AppResult<PostWithComments> {
        return postsEndpoint.getPostWithComments(subredditName, id).map {
            val post = it.post.toDomainPost().apply { subredditIcon = cache.getPost(id)?.subredditIcon  }
            cache.setPost(post)
            val comments = it.comments?.toDomainListing { it.toDomain(userProfileImageCache) }?.items ?: listOf()
            PostWithComments(post, comments)
        }
    }

    override suspend fun getPostsListing(
        subreddit: String,
        sortMode: PostSortMode,
        pageKey: String?,
        forceRefresh: Boolean
    ): AppResult<Listing<Post>> {

        Log.d("gePostListing$subreddit", pageKey.toString() + " $forceRefresh")

        if (!forceRefresh) {
            val cached = cache.getListing(subreddit, pageKey ?: FIRST_PAGE_KEY)
            if (cached != null) {
                Log.d("cacheNotNull", "notnull")
                return AppResult.success(cached)
            }
        }

        val response = postsEndpoint.getPosts(subreddit, sortMode.getUrl(), pageKey)

        return when (response) {
            is AppResult.Success -> {
                val dataListing = response.data
                var postListing = dataListing.toDomainListing { it.toDomainPost() }
                if (!VIDEOS_ENABLED) {
                    postListing = postListing.withoutVideos()
                }
                cache.setListing(subreddit, pageKey ?: FIRST_PAGE_KEY, postListing)
                AppResult.success(postListing)
            }
            is AppResult.Error -> response
        }
    }

    private fun Listing<Post>.withoutVideos() =
        copy(items = items.filter { it.content !is PostContent.Video }.toMutableList())

    private fun PostSortMode.getUrl() = "/" + this.name.toLowerCase()

    override suspend fun vote(post: Post, voteType: VoteType): AppResult<Unit> {

        return AppResult.success()

        // TODO add actual voting

      /*  val vote = when (voteType) {
            VoteType.UPVOTE -> 1
            VoteType.DOWNVOTE -> -1
            VoteType.NONE -> 0
        }

        post.vote = voteType
        cache.getPost(post.id)?.vote = voteType
        postsEndpoint.vote(post.fullId, vote)*/
    }

    override fun saveSortMode(subredditName: String, sortMode: PostSortMode) {
        sp.edit { putString(PREFS_SORT + subredditName, sortMode.toString()) }
    }

    override fun getSortMode(subredditName: String): PostSortMode {
        val str = sp.getString(PREFS_SORT + subredditName, null)
        return if (str != null) PostSortMode.valueOf(str) else PostSortMode.DEFAULT_MODE
    }

    companion object {
        private const val PREFS_SORT: String = "key_post_sort"
        private const val FIRST_PAGE_KEY: String = ""
        private const val VIDEOS_ENABLED = false
    }
}

