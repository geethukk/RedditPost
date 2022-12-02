package com.yourparkingspace.data.repositories

import com.yourparkingspace.domain.model.UserProfileImage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserProfileImageCache @Inject constructor() {

    // TODO add fetching actual profile images of users

    private val images = mutableMapOf<String, UserProfileImage>()

    fun getImageForUserId(id: String) : UserProfileImage {
        return images[id] ?: UserProfileImage.DefaultImage(UserProfileImage.randomColor).also {
            images[id] = it
        }
    }
}