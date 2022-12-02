package com.yourparkingspace.network.models

import com.google.gson.annotations.SerializedName

data class SubredditAboutDTO(
    @SerializedName("display_name")
    val name: String?,
    @SerializedName("subscribers")
    val subs: Int?,
    @SerializedName("public_description")
    val description: String?,
    @SerializedName("icon_img")
    val image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("accounts_active")
    val activeUsers: Int?,
    @SerializedName("key_color")
    val keyColor: String?,
    @SerializedName("community_icon")
    val icon: String?,
    @SerializedName("banner_background_image")
    val backgroundImage: String?,
    @SerializedName("banner_image")
    val bannerImage: String?
)