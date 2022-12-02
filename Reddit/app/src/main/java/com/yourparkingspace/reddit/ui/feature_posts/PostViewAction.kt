package com.yourparkingspace.reddit.ui.feature_posts

import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.reddit.ui.base.UiEvent

sealed class PostViewAction {
    object Click : PostViewAction()
    object ClickSubreddit : PostViewAction()
    object ClickAuthor : PostViewAction()
    object ClickImage : PostViewAction()
    data class ClickLink(val url: String) : PostViewAction()
    data class Vote(val upvoted: Boolean) : PostViewAction()
}

object PostViewEvents {
    data class ShowPostDetails(val post: Post) : UiEvent
    data class ShowPostImage(val post: Post) : UiEvent
    data class ShowSubreddit(val subredditName: String) : UiEvent
}

typealias PostActionCallback = (Post, PostViewAction) -> Unit
typealias PostContentCallback = (PostViewAction) -> Unit

