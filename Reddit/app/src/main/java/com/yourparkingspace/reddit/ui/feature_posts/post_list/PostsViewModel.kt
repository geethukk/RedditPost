package com.yourparkingspace.reddit.ui.feature_posts.post_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.FeedType
import com.yourparkingspace.domain.model.Listing
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.domain.model.PostSortMode
import com.yourparkingspace.domain.repository.IPostRepo
import com.yourparkingspace.domain.repository.ISubredditRepository
import com.yourparkingspace.domain.usecase.VoteUseCase
import com.yourparkingspace.reddit.ui.base.BaseViewModel
import com.yourparkingspace.reddit.ui.base.Events
import com.yourparkingspace.reddit.ui.common.utils.UiState
import com.yourparkingspace.reddit.ui.feature_posts.PostViewAction
import com.yourparkingspace.reddit.ui.feature_posts.PostViewEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepo: IPostRepo,
    private val subredditRepository: ISubredditRepository,
    private val voteUseCase: VoteUseCase
) : BaseViewModel() {

    var sub: String? = null
        private set

    private val posts: MutableList<Post> = mutableListOf()

    private val _sortMode: MutableStateFlow<PostSortMode> = MutableStateFlow(PostSortMode.DEFAULT_MODE)
    val sortMode = _sortMode.asStateFlow()

    private val _data: MutableStateFlow<UiState<List<Post>>> = MutableStateFlow(UiState.Loading())
    val data = _data.asStateFlow()

    val isRefreshing = MutableLiveData(false)

    var scrollPosition = 0
        private set

    private val pagedSource = PostsPagedSource()

    init {
        viewModelScope.launch {
            pagedSource.state.collect {
                Log.d("collected", it.toString())
                this@PostsViewModel._data.emit(it)
                if (it is UiState.Data) {
                    this@PostsViewModel.posts.apply {
                        clear()
                        addAll(it.data)
                    }
                    loadSubredditIcons(it.data)
                }
            }

            pagedSource.errors.collect {
                dispatchEvent(Events.ShowError(it))
            }
        }
    }

    fun setFeedType(feedType: FeedType) {
        when (feedType) {
            FeedType.Popular -> setSubreddit("Android")
            is FeedType.Subreddit -> setSubreddit(feedType.name)
            is FeedType.PostDetails -> {
            }
        }
    }

    private fun setSubreddit(name: String) {
        if (sub == null) {
            sub = name
            _sortMode.value = postRepo.getSortMode(name)
            retry()
        }
    }

    fun refreshData() {
        sub ?: return
        viewModelScope.launch {
            isRefreshing.value = true
            pagedSource.refresh()
            isRefreshing.value = false
        }
    }

    fun retry() {
        viewModelScope.launch {
            pagedSource.retry()
        }
    }

    fun updateScrollPosition(index: Int) {
        scrollPosition = index
        if (index == posts.lastIndex) {
            loadNextPage()
        }
    }

    fun handlePostAction(post: Post, action: PostViewAction) {
        when (action) {
            is PostViewAction.Click -> {
                dispatchNavigationEvent(PostViewEvents.ShowPostDetails(post))
            }
            is PostViewAction.Vote -> {
                viewModelScope.launch {
                    vote(post, action.upvoted)
                }
            }
            is PostViewAction.ClickSubreddit -> {
                dispatchNavigationEvent(
                    PostViewEvents.ShowSubreddit(
                        post.subreddit
                    )
                )

                viewModelScope.launch {
                    subredditRepository.visited(post.subreddit)
                }
            }
            is PostViewAction.ClickLink -> dispatchEvent(Events.OpenUrl(action.url))
            PostViewAction.ClickAuthor -> {
            }
            is PostViewAction.ClickImage -> dispatchEvent(
                PostViewEvents.ShowPostImage(post)
            )
        }
    }

    private fun loadNextPage() {
        if (sub != null) {
            viewModelScope.launch {
                pagedSource.loadNextPage()
            }
        }
    }

    fun changeSorting(sortMode: PostSortMode) {
        sub ?: return
        viewModelScope.launch {
            _sortMode.emit(sortMode)
            postRepo.saveSortMode(sub!!, sortMode)
            refreshData()
        }
    }

    private suspend fun vote(post: Post, vote: Boolean) {
        val result = voteUseCase.vote(post, vote)
        if (result is AppResult.Error) {
            dispatchEvent(Events.ShowError(result.error))
        } else {
            _data.emit(_data.value.copy())
        }
    }

    private suspend fun loadSubredditIcons(posts: List<Post>) {
        var didChange = false
        posts.chunked(10).forEach { list ->
            list.forEach {
                val subName = it.subreddit
                if (it.subredditIcon == null) {
                    it.subredditIcon = subredditRepository.getSubredditIcon(subName)
                    if (!didChange) {
                        didChange = it.subredditIcon != null
                    }
                }

                if (didChange) {
                    _data.emit(UiState.Data(posts))
                }
            }
        }
    }

    private inner class PostsPagedSource : PagedDataSource<Post>() {
        override val pageSize: Int
            get() = postRepo.pageSize

        override suspend fun loadDataForKey(
            key: String?,
            refresh: Boolean
        ): AppResult<Listing<Post>> {
            return postRepo.getPostsListing(sub!!, sortMode.value, key, refresh)
        }
    }
}