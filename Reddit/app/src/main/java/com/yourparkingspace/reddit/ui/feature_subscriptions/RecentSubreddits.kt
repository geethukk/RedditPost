package com.yourparkingspace.reddit.ui.feature_subscriptions
import com.yourparkingspace.reddit.R

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.reddit.ui.common.FontsSizes
import com.yourparkingspace.reddit.ui.common.composables.NetworkImage
import com.yourparkingspace.reddit.ui.common.composables.SubredditIcon
import com.yourparkingspace.reddit.ui.common.utils.formatNumber
import com.yourparkingspace.reddit.ui.theme.AppTheme
import com.yourparkingspace.reddit.ui.theme.isDark
import com.yourparkingspace.reddit.ui.theme.onBackgroundVariant

@Composable
fun RecentlyVisited(
    subreddits: List<Subreddit>,
    onClicked: (Subreddit) -> Unit,
    onDelete: (Subreddit) -> Unit
) {
    Column {
        Text(
            stringResource(R.string.subscriptions_recently_visited),
            modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 16.dp),
            color = MaterialTheme.colors.onBackgroundVariant,
            fontSize = FontsSizes.medium
        )
        LazyRow(Modifier.padding(vertical = 8.dp)) {
            itemsIndexed(subreddits) { index, item ->
                Spacer(Modifier.width(if (index == 0) 4.dp else 2.dp))
                RecentSubreddit(
                    subreddit = item,
                    onClicked = { onClicked(item) },
                    onDelete = { onDelete(item) },
                )
                Spacer(Modifier.width(if (index == subreddits.lastIndex) 4.dp else 2.dp))
            }
        }
    }
}

@Composable
private fun RecentSubreddit(
    subreddit: Subreddit,
    onClicked: () -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = if (subreddit.keyColor?.isNotEmpty() == true)
        Color(android.graphics.Color.parseColor(subreddit.keyColor))
    else
        MaterialTheme.colors.background

    Column(
        Modifier
            .height(165.dp)
            .width(200.dp)
            .background(MaterialTheme.colors.background)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClicked() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            NetworkImage(
                url = subreddit.backgroundImage,
                contentDescription = "Subreddit background image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(backgroundColor)
            )

            Box(Modifier
                .align(Alignment.BottomCenter)
            ) {
                SubredditIcon(
                    url = subreddit.icon,
                    size = 50.dp,
                    borderWidth = 2.dp
                )
            }

            IconButton(
                onClick = { onDelete() },
                modifier = Modifier
                    .padding(4.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color(0x33000000))
            ) {
                Icon(
                    Icons.Filled.Close,
                    "Delete from recently visited",
                    tint = Color.White
                )
            }
        }

        Column(
            Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = subreddit.prefixedName,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground,
            )
            Text(
                stringResource(R.string.subreddit_members, formatNumber(subreddit.subscriptions)),
                fontSize = FontsSizes.small,
                color = MaterialTheme.colors.onBackgroundVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = subreddit.description,
                color = MaterialTheme.colors.onBackground,
                fontSize = FontsSizes.small,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun RecentPreview() {

    val sub = Subreddit(
        name = "subreddit",
        description = "Here is the description of this awesome subreddit called r/subreddit, it should be limited to only three lines",
        subscriptions = 258000,
        activeUsers = 0,
        icon = null,
        backgroundImage = null,
        keyColor = null
    )

    isDark.value = true

    AppTheme {
        RecentSubreddit(sub, {}, {})
    }
}
