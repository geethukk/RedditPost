package com.yourparkingspace.reddit.ui.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) {
    Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(stringRes: Int) {
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
}

fun Activity.openUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}

fun Fragment.openUrl(url: String) {
    activity?.openUrl(url)
}

fun Activity.screenWidth(): Float {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val pixelsWidth = displayMetrics.widthPixels
    return convertPixelsToDp(pixelsWidth.toFloat(), this)
}

fun convertPixelsToDp(px: Float, context: Context): Float {
    return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}