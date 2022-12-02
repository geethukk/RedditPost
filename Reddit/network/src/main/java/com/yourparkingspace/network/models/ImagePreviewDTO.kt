package com.yourparkingspace.network.models

import com.google.gson.annotations.SerializedName

data class ImagePreviewDTO(
    @SerializedName("images")
    val images: List<RedditImagePreviewDTO>
)

data class RedditImagePreviewDTO(
    @SerializedName("resolutions")
    val resolutions: List<ImageResolutionDTO>
)

data class ImageResolutionDTO(
    @SerializedName("url")
    val url: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int
) {
    val aspectRatio: Float get() = height.toFloat() / width.toFloat()
}