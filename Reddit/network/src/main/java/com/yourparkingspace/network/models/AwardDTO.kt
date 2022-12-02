package com.yourparkingspace.network.models

import com.google.gson.annotations.SerializedName

data class AwardDTO(
    @SerializedName("icon_url")
    val icon: String,
    @SerializedName("resized_icons")
    val resizedIcons: List<ImageResolutionDTO>
)