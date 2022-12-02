package com.yourparkingspace.domain.model

sealed class UserProfileImage {
    class CustomImage(val url: String) : UserProfileImage()
    class DefaultImage(val color: Long) : UserProfileImage()

    companion object {
        private val defaultColors = listOf(
            0xfffcba03,
            0xfffc4e03,
            0xff4ccf06,
            0xff0a9dc2,
            0xff1244db,
            0xffba0ac7,
            0xffc70a78,
            0xffc70a40,
        )

        val randomColor: Long get() = defaultColors.random()
    }
}
