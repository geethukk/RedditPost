package com.yourparkingspace.reddit.ui.feature_subreddit_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourparkingspace.domain.model.FeedType
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.composables.DefaultSpacer
import com.yourparkingspace.reddit.ui.common.composables.NetworkImage
import com.yourparkingspace.reddit.ui.common.composables.SubredditIcon
import com.yourparkingspace.reddit.ui.common.composables.TopBar
import com.yourparkingspace.reddit.ui.feature_posts.post_list.PostsScreen
import com.yourparkingspace.reddit.ui.theme.AppTheme

@Composable
fun SubredditScreen(
    navController: NavController,
    model: SubredditViewModel = hiltViewModel(),
    subredditName: String
) {
    LaunchedEffect(key1 = "initSubreddit") {
        model.setSubreddit(subredditName)
    }

    val sub: Subreddit? by model.subreddit.observeAsState(null)
    val showLeaveDialog by model.showLeaveDialog.collectAsState()

    AppTheme {
        Box {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                TopBar()
                HeaderImages(sub)
                Spacer(modifier = Modifier.height(30.dp))
                SubredditHeaderInfo(
                    name = subredditName,
                    members = sub?.subscriptions ?: 0,
                    online = sub?.activeUsers ?: 0,
                    description = sub?.description ?: "",
                    subscriptionState = sub?.subscriptionState,
                    onJoinClicked = model::changeSubscriptionState
                )

                sub?.let {
                    PostsScreen(
                        feedType = FeedType.Subreddit(subredditName),
                        navController = navController,
                        withTopBar = false
                    )
                }
            }

            if (showLeaveDialog) {
                LeaveSubredditDialog(
                    subredditName = sub?.name.toString(),
                    onConfirm = model::confirmLeave,
                    onDismiss = model::cancelLeaving
                )
            }
        }
    }
}

@Composable
private fun LeaveSubredditDialog(
    modifier: Modifier = Modifier,
    subredditName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        backgroundColor = MaterialTheme.colors.surface,
        buttons = {
            LeaveDialogBody(
                subredditName = subredditName,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
private fun LeaveDialogBody(
    subredditName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.subreddit_leave_question, subredditName))
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Button(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f),
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    stringResource(R.string.common_cancel),
                    style = TextStyle(color = Color.White)
                )
            }

            DefaultSpacer(size = 8.dp)

            Button(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f),
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    stringResource(R.string.subreddit_leave),
                    style = TextStyle(color = Color.White)
                )
            }
        }
    }
}

@Composable
private fun HeaderImages(sub: Subreddit?) {
    val backgroundColor = if (sub?.keyColor?.isNotEmpty() == true)
        Color(android.graphics.Color.parseColor(sub.keyColor))
    else
        MaterialTheme.colors.background

    Box(modifier = Modifier.height(180.dp)) {
        NetworkImage(
            url = sub?.backgroundImage,
            contentDescription = "Subreddit header image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(backgroundColor)
        )

        Box(Modifier
            .align(Alignment.BottomStart)
            .padding(start = 16.dp)
        ) {
            SubredditIcon(
                url = sub?.icon,
                size = 60.dp,
                borderWidth = 3.dp,
                onClick = {}
            )
        }
    }
}
