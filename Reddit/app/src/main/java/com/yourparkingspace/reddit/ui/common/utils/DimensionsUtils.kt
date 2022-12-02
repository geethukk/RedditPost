package com.yourparkingspace.reddit.ui.common.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getNavBarHeightDp(): Float {
    return convertPixelsToDp(getNavBarHeight().toFloat(), this)
}

fun Context.getStatusBarHeightDp(): Float {
    return convertPixelsToDp(getStatusBarHeight().toFloat(), this)
}

fun Context.getNavBarHeight(): Int {
    val result = 0
    val hasMenuKey: Boolean = ViewConfiguration.get(this).hasPermanentMenuKey()
    val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    if (!hasMenuKey && !hasBackKey) {
        //The device has a navigation bar
        val resources: Resources = resources
        val orientation: Int = resources.configuration.orientation
        val resourceId: Int
        resourceId = if (isTablet(this)) {
            resources.getIdentifier(
                if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_height_landscape",
                "dimen",
                "android"
            )
        } else {
            resources.getIdentifier(
                if (orientation == Configuration.ORIENTATION_PORTRAIT) "navigation_bar_height" else "navigation_bar_width",
                "dimen",
                "android"
            )
        }
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

private fun isTablet(c: Context): Boolean {
    return (c.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}