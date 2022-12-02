package com.yourparkingspace.reddit.ui.feature_posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourparkingspace.domain.model.Awards
import com.yourparkingspace.domain.model.Flair
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.*
import com.yourparkingspace.reddit.ui.common.composables.DefaultSpacer
import com.yourparkingspace.reddit.ui.common.composables.NetworkImage
import com.yourparkingspace.reddit.ui.common.composables.SubredditIcon
import com.yourparkingspace.reddit.ui.common.composables.VoteButtons
import com.yourparkingspace.reddit.ui.common.utils.formatPostDate
import com.yourparkingspace.reddit.ui.common.utils.formatPostScore
import com.yourparkingspace.reddit.ui.theme.onBackgroundVariant

enum class PostViewType {
    MAIN_PAGE,
    SUBREDDIT,
    FULL_POST,
    PROFILE;

    fun isFullPost() = this == FULL_POST
}

@Composable
fun PostView(
    post: Post,
    viewType: PostViewType,
    callback: PostActionCallback,
    showVoteBar: Boolean = true
) {
    Column(
        Modifier
            .fillMaxWidth()
            .then(if (!viewType.isFullPost())
                Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = Color.White)
                ) { callback(post, PostViewAction.Click) } else Modifier)

            .background(MaterialTheme.colors.background)
            .padding(top = if (viewType != PostViewType.SUBREDDIT) 8.dp else 0.dp)

    ) {
        PostHeader(post = post, viewType, callback)
        if (viewType != PostViewType.SUBREDDIT)
            DefaultSpacer()
        if (post.awards.count > 0) {
            PostAwards(awards = post.awards)
            DefaultSpacer()
        }
        PostContent(
            content = post.content,
            showFull = viewType.isFullPost(),
            callback = { action -> callback(post, action) })

        if (showVoteBar) {
            PostActions(post = post, callback)
        }
    }
}

@Composable
fun PostAwards(awards: Awards) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        for (i in 0 until awards.icons.size.coerceAtMost(5))
            AwardIcon(url = awards.icons[i])
        DefaultSpacer()
        Text(text = "${awards.count} Awards", color = Color.Gray, fontSize = FontsSizes.small)
    }
}

@Composable
private fun AwardIcon(url: String) {
    NetworkImage(
        url = url,
        contentDescription = "Award icon",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(18.dp)
            .padding(2.dp)
        )
}

@Composable
fun PostFlair(flair: Flair) {
    Card(shape = RoundedCornerShape(6.dp), backgroundColor = Color(flair.color)) {
        Text(
            flair.text,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 10.sp
        )
    }
}

@Composable
fun PostActions(
    post: Post,
    callback: PostActionCallback,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VoteButtons(formatPostScore(post.score), post.vote, !post.isArchived) {
            callback(
                post,
                PostViewAction.Vote(it)
            )
        }
        IconTextButton(
            icon = Icons.Outlined.Comment,
            text = post.commentCount.toString(),
            onClick = {}
        )
        IconTextButton(
            icon = Icons.Outlined.Share,
            text = stringResource(R.string.post_share),
            onClick = {}
        )
    }
}

@Composable
fun IconTextButton(icon: ImageVector, text: String? = null, onClick: () -> Unit) {
    Row(
        Modifier.height(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClick, modifier = Modifier.width(20.dp)) {
            Icon(
                imageVector = icon, contentDescription = "", tint = Color.Gray,
            )
        }
        if (text != null) {
            DefaultSpacer()
            Text(
                text = text,
                color = Color.Gray,
                fontSize = FontsSizes.medium,
                modifier = Modifier.padding(horizontal = Dimensions.padding)
            )
        }
    }
}

@Composable
private fun PostHeader(
    post: Post,
    postViewType: PostViewType,
    callback: PostActionCallback,
    modifier: Modifier = Modifier
) {

    val isSubredditView = postViewType == PostViewType.SUBREDDIT

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isSubredditView) {
            SubredditIcon(
                url = post.subredditIcon,
                onClick = {
                    callback(post, PostViewAction.ClickSubreddit)
                }
            )
            DefaultSpacer()
        }

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
            if (!isSubredditView)
                Text(
                    text = post.subredditPrefixed,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp
                )
            Text(
                text = post.authorPrefixed + " · " + formatPostDate(post.date),
                color = MaterialTheme.colors.onBackgroundVariant,
                fontSize = 12.sp
            )
        }

        if (postViewType != PostViewType.FULL_POST) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}
