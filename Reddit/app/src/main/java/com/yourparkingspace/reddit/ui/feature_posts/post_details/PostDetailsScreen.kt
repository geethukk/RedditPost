package com.yourparkingspace.reddit.ui.feature_posts.post_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.reddit.ui.common.composables.SubredditIcon
import com.yourparkingspace.reddit.ui.common.composables.UiEventHandler
import com.yourparkingspace.reddit.ui.common.utils.UiState
import com.yourparkingspace.reddit.ui.feature_posts.*
import com.yourparkingspace.reddit.ui.theme.AppTheme
import com.yourparkingspace.reddit.ui.theme.backgroundDark

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailsScreen(
    navController: NavController,
    model: PostDetailsViewModel = hiltViewModel(),
    postId: String,
    subredditName: String
) {
    val state by model.post.collectAsState()

    val postCallback: PostActionCallback = { p, action ->
        if (action is PostViewAction.Vote) {
            model.vote(p, action.upvoted)
        }
    }
    val listState = rememberLazyListState()
    val isRefreshing by model.isRefreshing.collectAsState()

    LaunchedEffect(key1 = "loadPost", block = {
        model.loadPost(postId, subredditName)
    })

    AppTheme {
        val currentState = state
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
            //.padding(bottom = 50.dp, top = 30.dp)
        ) {
            UiEventHandler(model = model, navController = navController) {
                PostDetailsTopBar(
                    navController = navController,
                    post = (currentState as? UiState.Data)?.data,
                    listState = listState
                )
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    indicator = { refreshState, triggerDistance ->
                        SwipeRefreshIndicator(
                            state = refreshState,
                            refreshTriggerDistance = triggerDistance,
                            contentColor = MaterialTheme.colors.primary
                        )
                    },
                    onRefresh = { model.refreshData() }
                ) {
                    if (currentState is UiState.Data) {
                        Box(Modifier.fillMaxHeight()) {
                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                                item {
                                    PostView(
                                        currentState.data,
                                        viewType = PostViewType.FULL_POST,
                                        postCallback,
                                        showVoteBar = false,
                                    )
                                }

                                stickyHeader {
                                    PostActions(
                                        post = currentState.data,
                                        postCallback,
                                        modifier = Modifier.background(MaterialTheme.colors.background)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostDetailsTopBar(navController: NavController, post: Post?, listState: LazyListState) {

    val alpha: Float = when {
        listState.firstVisibleItemIndex > 0 -> 1.0f
        else -> (listState.firstVisibleItemScrollOffset / 200f).coerceIn(0f, 1f)
    }

    Column {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.alpha(alpha)
                ) {
                    SubredditIcon(url = post?.subredditIcon, size = 20.dp)
                    Spacer(modifier = Modifier.size(10.dp))
                    post?.subreddit?.let {
                        Text(
                            text = post.subredditPrefixed,
                            style = TextStyle(
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colors.onBackground,
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.backgroundDark,
            contentColor = MaterialTheme.colors.onBackground,
        )

        Spacer(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .height(0.5.dp)
        )
    }
}
