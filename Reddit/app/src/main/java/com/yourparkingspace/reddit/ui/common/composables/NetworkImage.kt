package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String,
    contentScale: ContentScale = ContentScale.Crop,
    loading: @Composable () -> Unit = { DefaultImageLoadingPlaceholder() },
    error: @Composable () -> Unit = { DefaultImageErrorPlaceholder() }
) {
    Image(
        painter = rememberImagePainter(url, builder = {
            crossfade(true)
        }),
        contentScale = contentScale,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
fun DefaultImageLoadingPlaceholder() {
    // TODO
}

@Composable
fun DefaultImageErrorPlaceholder() {
    // TODO
}