package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SubredditIcon(
    url: String?,
    size: Dp = 35.dp,
    borderWidth: Dp = 0.dp,
    onClick: (() -> Unit)? = null
) {
    Box(Modifier
        .clip(CircleShape)
        .then(
            if (borderWidth != 0.dp) Modifier.border(
                borderWidth,
                Color.White,
                CircleShape
            ) else Modifier
        )
        .background(MaterialTheme.colors.background)
        .then(if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier)
    ) {
        if (url != null) {
            NetworkImage(
                url = url,
                contentDescription = "Subreddit icon",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            Surface(
                modifier = Modifier.size(size),
                shape = CircleShape,
                color = Color.Gray,

                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "r/")
                    }
                }
            )
        }
    }
}
