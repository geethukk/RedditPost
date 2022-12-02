package com.yourparkingspace.reddit.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

val DarkTheme = darkColors(primary = Color(0xFF107aeb))

val LightTheme = lightColors(primary = Color(0xFF107aeb))

val isDark = mutableStateOf(true)

fun changeTheme() {
    isDark.value = !isDark.value
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val dark: Boolean by isDark

    MaterialTheme(colors = if (dark) DarkTheme else LightTheme) {
        content()
    }
}

val Colors.imageOverlayBackground get() = Color(0xCC000000)

val Colors.onBackgroundVariant get() = Color(0xFF828582)

val Colors.backgroundDark get() = if (isDark.value) Color.Black else Color.LightGray

val Colors.inputBackground get() = if (isDark.value) Color(0xFF232423) else Color.LightGray

val Colors.onInputBackground get() = if (isDark.value) onBackgroundVariant else onBackground