package com.yourparkingspace.reddit.ui.theme

import androidx.compose.ui.graphics.Color

object AppColors {

    val blue = getColor("149EF0")
    val upvote = getColor("FF4300")
    val downvote = getColor("9494FF")

    private fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor("#$colorString"))
    }
}


