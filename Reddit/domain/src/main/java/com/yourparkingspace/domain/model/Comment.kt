package com.yourparkingspace.domain.model

sealed class CommentItem(
    val depth: Int
)

data class Comment(
    val id: String,
    val text: String,
    val authorId: String,
    val authorName: String,
    val authorImage: UserProfileImage,
    val date: Long,
    val score: Int,
    val myVote: VoteType,
    val awards: Awards,
    val replies: List<CommentItem>,
    private val itemDepth: Int,
    val isPostAuthor: Boolean,
    var isFirstReply: Boolean = false,
    var isLastReply: Boolean = false,
    val isArchived: Boolean = false
) : CommentItem(itemDepth)

data class MoreComments(
    val count: Int,
    private val itemDepth: Int,
    var isFirstReply: Boolean = false,
    var isLastReply: Boolean = false,
) : CommentItem(itemDepth)
