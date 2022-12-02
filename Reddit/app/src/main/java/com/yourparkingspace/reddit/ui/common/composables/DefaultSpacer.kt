package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSpacer(size: Dp = 5.dp) {
    Spacer(modifier = Modifier.size(size))
}