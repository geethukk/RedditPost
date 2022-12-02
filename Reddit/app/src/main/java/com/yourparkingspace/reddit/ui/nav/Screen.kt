package com.yourparkingspace.reddit.ui.nav

import androidx.annotation.StringRes
import com.yourparkingspace.reddit.R

sealed class Screen(
    val route: String,
    @StringRes val titleResId: Int
) {
    // top level screen with bottom nav
    object MainScreen : Screen("main", R.string.screen_title_main)

    // bottom nav destinations
    object Home : Screen("home", R.string.screen_title_home)
    object Subscriptions : Screen("subscriptions", R.string.screen_title_subscriptions)
    object Profile : Screen("profile", R.string.screen_title_profile)

    // full screen detail destinations
    object PostDetails : Screen("post_details", R.string.screen_title_post_details)
    object PostImage : Screen("post_image", R.string.screen_title_post_details)
    object SubredditDetails : Screen("subreddit_details", R.string.screen_title_subreddit_details)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}