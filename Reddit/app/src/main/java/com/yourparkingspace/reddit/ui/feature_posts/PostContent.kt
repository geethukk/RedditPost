package com.yourparkingspace.reddit.ui.feature_posts

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.MediaItem
import com.yourparkingspace.domain.model.PostContent
import com.yourparkingspace.reddit.ui.common.FontsSizes
import com.yourparkingspace.reddit.ui.common.composables.DefaultSpacer
import com.yourparkingspace.reddit.ui.common.composables.NetworkImage
import com.yourparkingspace.reddit.ui.common.utils.getWebsiteFromUrl
import com.yourparkingspace.reddit.ui.common.utils.screenWidth
import com.yourparkingspace.reddit.ui.theme.imageOverlayBackground

@Composable
fun PostContent(content: PostContent, showFull: Boolean, callback: PostContentCallback) {
    when (content) {
        is PostContent.Text -> PostContentText(content, showFull)
        is PostContent.Image -> PostContentImage(content, showFull) {
            callback(PostViewAction.ClickImage)
        }
        is PostContent.Video -> PostContentVideo(content, showFull)
        is PostContent.Link -> PostContentLink(content, showFull) {
            callback(PostViewAction.ClickLink(content.url))
        }
        is PostContent.Images -> PostContentImages(content, showFull)
    }
}

@Composable
private fun PostTitle(
    title: String,
    showFull: Boolean,
    modifier: Modifier = Modifier,
    maxLines: Int = if (showFull) 5 else 3
) {
    Text(
        text = title,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colors.onBackground,
        fontSize = FontsSizes.big,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colors.background)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PostContentImages(content: PostContent.Images, showFull: Boolean) {

    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxWidth()) {
        PostTitle(title = content.title, showFull)
        DefaultSpacer()
        HorizontalPager(count = content.images.size,state = pagerState) { page ->
            val image = content.images[page]
            NetworkImage(
                url = image.url,
                contentDescription = "Post image $page",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .clickable { },
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colors.primary,
            inactiveColor = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }
}

@Composable
private fun PostContentText(content: PostContent.Text, showFull: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PostTitle(title = content.title, showFull)
        DefaultSpacer()
        Text(
            text = content.text,
            maxLines = if (showFull) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            color = if (showFull) MaterialTheme.colors.onBackground else Color.Gray,
            fontSize = if (showFull) FontsSizes.medium else FontsSizes.small,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun PostContentImage(content: PostContent.Image, showFull: Boolean, onClick: () -> Unit) {
    val screenWidth = (LocalContext.current as Activity).screenWidth()
    val imageHeight = (screenWidth * content.image.aspectRation).dp

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
    ) {
        PostTitle(title = content.title, showFull)
        DefaultSpacer()
        NetworkImage(
            url = content.image.url,
            contentDescription = "Post image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxWidth()
                .height(imageHeight)
                .clickable { onClick() },
        )
    }
}

@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.packageName)
            )

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

            this.prepare(source)
        }
    }

    AndroidView(modifier = Modifier.height(350.dp), factory = { ctx ->
        PlayerView(context).apply {
            player = exoPlayer
        }
    })
}

@Composable
private fun PostContentVideo(content: PostContent.Video, showFull: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PostTitle(title = content.title, showFull)
        DefaultSpacer()
        VideoPlayer(content.url)
    }
}

@Composable
private fun PostContentLink(content: PostContent.Link, showFull: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        PostTitle(
            title = content.title,
            showFull = showFull,
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
        )

        DefaultSpacer()
        PostLinkThumbnail(
            url = content.thumbnail,
            shortUrl = getWebsiteFromUrl(content.url),
            modifier = Modifier
                .widthIn(120.dp, 120.dp)
                .heightIn(80.dp, 80.dp)
                .padding(end = 16.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
private fun PostLinkThumbnail(url: String, shortUrl: String, modifier: Modifier) {
    Box(
        modifier
            .width(180.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        NetworkImage(
            url = url,
            contentDescription = "Post link thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = shortUrl,
            maxLines = 1,
            fontSize = 10.sp,
            modifier = Modifier
                .background(MaterialTheme.colors.imageOverlayBackground)
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            color = Color.White
        )
    }
}