package com.yourparkingspace.network.models

import com.google.gson.annotations.SerializedName

data class PostDTO(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val fullId: String?,
    val title: String?,
    val subreddit: String?,
    val thumbnail: String?,
    val author: String?,
    @SerializedName("selftext")
    val selfText: String?,
    @SerializedName("created_utc")
    val created: Long?,
    val score: Int?,
    @SerializedName("num_comments")
    val commentsCount: Int?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("post_hint")
    val postHint: String?,
    @SerializedName("preview")
    val imagePreview: ImagePreviewDTO?,
    @SerializedName("total_awards_received")
    val awardCount: Int?,
    @SerializedName("all_awardings")
    val awards: List<AwardDTO>?,
    @SerializedName("archived")
    val isArchived: Boolean?
)

