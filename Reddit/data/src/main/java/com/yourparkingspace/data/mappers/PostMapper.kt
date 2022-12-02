package com.yourparkingspace.data.mappers

import com.yourparkingspace.domain.model.*
import com.yourparkingspace.network.models.PostDTO

internal fun PostDTO.toDomainPost() = Post(
    id = this.id.orEmpty(),
    fullId = this.fullId.orEmpty(),
    title = this.title.orEmpty(),
    subreddit = this.subreddit.orEmpty(),
    author = this.author.orEmpty(),
    commentCount = this.commentsCount ?: 0,
    score = this.score ?: 0,
    date = this.created ?: 0,
    vote = VoteType.NONE,
    content = this.getContent(),
    awards = Awards(this.awardCount ?: 0, this.awards?.map { it.icon } ?: listOf()),
    isArchived = isArchived ?: false
)

private fun PostDTO.getContent(): PostContent {

    return when {
        !selfText.isNullOrEmpty() -> PostContent.Text(title.orEmpty(), selfText!!)
        postHint == "image" -> createImageContent(this)
        postHint == "link" -> PostContent.Link(title.orEmpty(), url.orEmpty(), thumbnail.orEmpty())
        postHint?.contains("video") == true -> PostContent.Video(title.orEmpty(), url.orEmpty())
        else -> PostContent.Text(title.orEmpty(), "")
    }
}

private fun createImageContent(postDTO: PostDTO): PostContent.Image {

    val aspectRatio = postDTO
        .imagePreview
        ?.images
        ?.firstOrNull()
        ?.resolutions
        ?.firstOrNull()
        ?.aspectRatio ?: 1f

    return PostContent.Image(
        postDTO.title.orEmpty(),
        PostImage(
            postDTO.url.orEmpty(),
            aspectRatio
        )
    )

}
