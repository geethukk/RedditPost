package com.yourparkingspace.domain.model

data class Post(
    val id: String,
    val fullId: String,
    val title: String,
    val subreddit: String,
    val author: String,
    val commentCount: Int,
    var score: Int,
    val date: Long,
    var subredditIcon: String? = null,
    var vote: VoteType,
    val content: PostContent,
    val awards: Awards,
    val isArchived: Boolean
) {
    val subredditPrefixed get() = "r/$subreddit"
    val authorPrefixed get() = "u/$author"
}

sealed class PostContent(val title: String) {
    class Text(title: String, val text: String) : PostContent(title)
    class Image(title: String, val image: PostImage) : PostContent(title)
    class Images(title: String, val images: List<PostImage>) : PostContent(title)
    class Video(title: String, val url: String) : PostContent(title)
    class Link(title: String, val url: String, val thumbnail: String) : PostContent(title)
}

data class PostImage(val url: String, val aspectRation: Float)

data class Awards(
    val count: Int,
    val icons: List<String>
)

data class Flair(
    val text: String,
    val color: ULong
)