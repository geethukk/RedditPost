package com.yourparkingspace.reddit.ui.feature_posts.post_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yourparkingspace.domain.model.FeedType
import com.yourparkingspace.domain.model.Post
import com.yourparkingspace.domain.model.PostSortMode
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.composables.*
import com.yourparkingspace.reddit.ui.common.utils.PaginationLoadingState
import com.yourparkingspace.reddit.ui.common.utils.UiState
import com.yourparkingspace.reddit.ui.feature_posts.PostView
import com.yourparkingspace.reddit.ui.feature_posts.PostViewType
import com.yourparkingspace.reddit.ui.theme.AppTheme
import com.yourparkingspace.reddit.ui.theme.backgroundDark

@Composable
fun PostsScreen(
    feedType: FeedType,
    navController: NavController,
    model: PostsViewModel = hiltViewModel(),
    withTopBar: Boolean = true,
    listHeader: (@Composable () -> Unit)? = null
) {
    AppTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Box(Modifier.fillMaxSize()) {
                UiEventHandler(model = model, navController = navController) {
                    Column {
                        if (withTopBar) TopBar()
                        PostList(feedType, model, navController, listHeader)
                    }
                }
            }
        }
    }
}

class PostListStateViewModel : ViewModel() {
    val listState = LazyListState()
}

@Composable
fun PostList(
    feedType: FeedType,
    model: PostsViewModel,
    navController: NavController,
    listHeader: (@Composable () -> Unit)? = null
) {
    val listStateViewModel: PostListStateViewModel = viewModel()
    val isRefreshing by model.isRefreshing.observeAsState(false)
    val postsState: UiState<List<Post>> by model.data.collectAsState()

    LaunchedEffect(key1 = "initSubredditName") {
        model.setFeedType(feedType)
    }

    val viewType = when (feedType) {
        FeedType.Popular -> PostViewType.MAIN_PAGE
        is FeedType.PostDetails -> PostViewType.FULL_POST
        is FeedType.Subreddit -> PostViewType.SUBREDDIT
    }

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
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            state = listStateViewModel.listState
        ) {
            if (listHeader != null) {
                item {
                    listHeader()
                }
            }

            when (val state = postsState) {
                is UiState.Data<List<Post>> -> {
                    item { SortSelector(model) }
                    itemsIndexed(state.data) { index, post ->
                        model.updateScrollPosition(index)
                        PostView(
                            post = post,
                            callback = model::handlePostAction,
                            viewType = viewType
                        )
                        Divider(color = MaterialTheme.colors.backgroundDark, thickness = 10.dp)
                    }
                    if (state.nextPageLoading != PaginationLoadingState.Completed)
                        item {
                            PageLoadingItem(
                                state = state.nextPageLoading,
                                onRetry = { model.retry() })
                        }
                }
                is UiState.Error -> item {
                    Box(Modifier.fillParentMaxSize()) {
                        ErrorScreen(
                            error = state.error,
                            onRetry = { model.retry() }
                        )
                    }
                }
                is UiState.Loading -> item {
                    Box(Modifier.fillParentMaxSize()) {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SortSelector(model: PostsViewModel) {
    val sort: PostSortMode by model.sortMode.collectAsState()
    PostSortModeSelector(
        selectedMode = sort,
        onSelect = model::changeSorting
    )
}

@Composable
fun PageLoadingItem(state: PaginationLoadingState, onRetry: () -> Unit) {
    if (state != PaginationLoadingState.Completed) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                PaginationLoadingState.Error -> PaginationLoadError {
                    onRetry()
                }
                PaginationLoadingState.Loading -> ProgressIndicator()
                else -> {}
            }
        }
    }
}

@Composable
fun PaginationLoadError(onClick: () -> Unit) {
    Column(
        Modifier.height(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_generic),
            color = MaterialTheme.colors.onBackground
        )
        DefaultSpacer(size = 10.dp)
        Button(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.primary),
            onClick = onClick
        ) {
            Text(
                text = stringResource(R.string.error_retry),
                modifier = Modifier.width(100.dp)
            )
        }
    }
}
