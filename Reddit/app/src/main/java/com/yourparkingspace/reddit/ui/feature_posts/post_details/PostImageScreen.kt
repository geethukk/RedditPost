package com.yourparkingspace.reddit.ui.feature_posts.post_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.domain.model.PostContent
import com.yourparkingspace.reddit.ui.common.composables.NetworkImage
import com.yourparkingspace.reddit.ui.common.utils.formatPostDate
import com.yourparkingspace.reddit.ui.feature_posts.PostActions
import com.yourparkingspace.reddit.ui.theme.changeTheme
import com.yourparkingspace.reddit.ui.theme.imageOverlayBackground
import kotlin.math.roundToInt

// TODO add navigating to this screen

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PostImageScreen(post: Post, content: PostContent.Image) {

    val text = "${post.subredditPrefixed} · ${post.authorPrefixed} · ${formatPostDate(post.date)}"
    var showUi: Boolean by remember {
        mutableStateOf(true)
    }
    val spec: FiniteAnimationSpec<Float> = tween(durationMillis = 100, easing = LinearEasing)
    val fadeIn = fadeIn(animationSpec = spec)
    val fadeOut = fadeOut(animationSpec = spec)

    Box {

        ZoomableImage(url = content.image.url, onTap = { showUi = !showUi })

        AnimatedVisibility(
            visible = showUi,
            enter = fadeIn,
            exit = fadeOut
        ) {
            ImageHeader(
                subtitle = text,
                title = post.title,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        AnimatedVisibility(
            visible = showUi,
            enter = fadeIn,
            exit = fadeOut,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            PostActions(
                post = post,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colors.imageOverlayBackground)
                    .padding(8.dp),
                callback = { i, k -> }
            )
        }
    }
}

@Composable
fun BackArrow(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = modifier
                .size(25.dp)
                .shadow(elevation = 0.dp),
            tint = Color.White,
            imageVector = Icons.Outlined.ArrowBack, contentDescription = ""
        )
    }
}

@Composable
fun ImageHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.imageOverlayBackground)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackArrow(onClick = {
            changeTheme()
        })

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
            Text(text = subtitle, color = Color.White, fontSize = 12.sp)
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = { },
            modifier = Modifier
                .wrapContentWidth()
                .width(20.dp),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = com.yourparkingspace.reddit.R.drawable.ic_baseline_more_vert_24),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier
                    .size(25.dp)
                    .shadow(elevation = 0.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ZoomableImage(url: String, onTap: () -> Unit) {
    var scale: Float by remember {
        mutableStateOf(1f)
    }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    scale *= zoom
                }
            }
    ) {
        NetworkImage(
            url = url,
            contentDescription = "Post image",
            modifier = Modifier
                .align(Alignment.Center)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onDoubleClick = {
                        scale = if (scale > 1) 1f else 2f
                        offsetX.value = 0f
                        offsetY.value = 0f
                    },
                    onClick = {
                        onTap()
                    }
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        val maxOffX = if (scale > 1) 100f * scale else 0f
                        val maxOffY = if (scale > 1) 100f * scale else 0f
                        offsetX.value = (offsetX.value + dragAmount.x).coerceIn(-maxOffX, maxOffX)
                        offsetY.value = (offsetY.value + dragAmount.y).coerceIn(-maxOffY, maxOffY)
                    }
                }
                .offset(offsetX.value.roundToInt().dp, offsetY.value.roundToInt().dp)
                .graphicsLayer(
                    // adding some zoom limits (min 50%, max 200%)
                    scaleX = maxOf(1f, minOf(2f, scale)),
                    scaleY = maxOf(1f, minOf(2f, scale))
                ),
        )
    }
}

@Preview
@Composable
private fun ImageHeaderPreview() {
    ImageHeader(
        title = "post title",
        subtitle = "post subtitle"
    )
}