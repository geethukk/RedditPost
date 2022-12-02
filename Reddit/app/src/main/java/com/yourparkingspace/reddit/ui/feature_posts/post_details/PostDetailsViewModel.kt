package com.yourparkingspace.reddit.ui.feature_posts.post_details

import androidx.lifecycle.viewModelScope
import com.yourparkingspace.common_utils.AppError
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.domain.model.*
import com.yourparkingspace.domain.repository.IAuthenticator
import com.yourparkingspace.domain.repository.ICommentRepository
import com.yourparkingspace.domain.repository.IPostRepo
import com.yourparkingspace.domain.usecase.VoteUseCase
import com.yourparkingspace.reddit.ui.base.BaseViewModel
import com.yourparkingspace.reddit.ui.base.Events
import com.yourparkingspace.reddit.ui.common.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias CommentsState = UiState<List<CommentItem>>

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepo: IPostRepo,
    private val commRepo: ICommentRepository,
    private val authenticator: IAuthenticator,
    private val voteUseCase: VoteUseCase
) : BaseViewModel() {

    private val _post: MutableStateFlow<UiState<Post>> = MutableStateFlow(UiState.Loading())
    val post = _post.asStateFlow()

    private val _comments: MutableStateFlow<CommentsState> = MutableStateFlow(UiState.Loading())
    val comments = _comments.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _showCommentInput = MutableStateFlow(authenticator.isLoggedIn())
    val showCommentInput = _showCommentInput.asStateFlow()

    private val _commentSortMode = MutableStateFlow(CommentSortMode.DEFAULT_MODE)
    val commentSortMode = _commentSortMode.asStateFlow()

    private var postId: String? = null

    fun refreshData() {
        val post = (_post.value as? UiState.Data)?.data
        post?.let {
            viewModelScope.launch {
                _isRefreshing.emit(true)
                val result = postRepo.getPostWithComments(post.id, post.subreddit)
                when (result) {
                    is AppResult.Error -> dispatchEvent(Events.ShowError(result.error))
                    is AppResult.Success -> {
                        _post.emit(UiState.Data(result.data.post))
                        emitComments(result.data.comments)
                    }
                }
                _isRefreshing.emit(false)
            }
        }
    }

    fun loadPost(id: String, subredditName: String) {
        postId = id
        viewModelScope.launch {
            val post = postRepo.getPost(id, subredditName)
            if (post != null) {
                _post.emit(UiState.Data(post))
                loadComments(post)
            } else {
                _post.emit(UiState.Error(AppError.NotFound))
            }
        }
    }

    fun showMoreComments(more: MoreComments) {
        // TODO add implementation
    }

    fun sortComments(mode: CommentSortMode) {
        // TODO add implementation
    }

    private suspend fun emitComments(comments: List<CommentItem>) {
        val sorted = comments.sortedByDescending {
            when (it) {
                is Comment -> it.score
                is MoreComments -> Int.MIN_VALUE
            }
        }
        val c = mutableListOf<CommentItem>()
        sorted.forEach { it.flatten(c) }
        c.count { it is MoreComments }
        emitCommentState(UiState.Data(c))
    }

    private suspend fun loadComments(post: Post) {
        when (val result = commRepo.getComments(post)) {
            is AppResult.Error -> emitCommentState(UiState.Error(result.error))
            is AppResult.Success -> emitComments(result.data)
        }
    }

    private suspend fun emitCommentState(state: CommentsState) {
        _comments.emit(state)
    }

    fun vote(post: Post, vote: Boolean) {
        viewModelScope.launch {
            when (val result = voteUseCase.vote(post, vote)) {
                is AppResult.Error -> dispatchEvent(Events.ShowError(result.error))
                is AppResult.Success -> {
                    _post.emit(UiState.Data(result.data))
                }
            }
        }
    }

    fun vote(comment: Comment, vote: Boolean) {
        viewModelScope.launch {
            if (authenticator.isLoggedIn()) {
                // TODO add voting
            } else {
                dispatchEvent(Events.ShowError(AppError.Unauthenticated))
            }
        }
    }

    private fun CommentItem.flatten(list: MutableList<CommentItem>) {
        list.add(this)
        if (this is Comment) {
            replies.forEachIndexed { index, reply ->
                if (reply is Comment) {
                    reply.isLastReply = index == replies.lastIndex && reply.replies.isEmpty()
                    reply.isFirstReply = index == 0
                }
                reply.flatten(list)
            }
        }
    }
}

