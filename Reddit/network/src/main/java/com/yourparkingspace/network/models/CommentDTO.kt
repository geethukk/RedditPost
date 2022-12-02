package com.yourparkingspace.network.models

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

sealed class CommentResponseDTO

data class CommentDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("body")
    val body: String?,
    @SerializedName("author")
    val authorName: String?,
    @SerializedName("author_fullname")
    val authorId: String?,
    @SerializedName("created")
    val dateCreated: Long,
    @SerializedName("score")
    val score: Int,
    @SerializedName("total_awards_received")
    val totalAwardsReceived: Int,
    @SerializedName("parent_id")
    val parentId: String,
    @SerializedName("depth")
    val depth: Int,
    @SerializedName("is_submitter")
    val isPostAuthor: Boolean,
    @SerializedName("replies")
    internal val repliesJson: JsonElement,
) : CommentResponseDTO() {

    @Expose(deserialize = false)
    var childComments: ListingResponse<CommentResponseDTO>? = null
}

data class MoreDTO(
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("parent_id")
    val parentId: String,
    @SerializedName("children")
    val children: List<String>,
    @SerializedName("depth")
    val depth: Int
) : CommentResponseDTO()
