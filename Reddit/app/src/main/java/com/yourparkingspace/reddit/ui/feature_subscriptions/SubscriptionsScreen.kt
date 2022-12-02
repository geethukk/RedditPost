package com.yourparkingspace.reddit.ui.feature_subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourparkingspace.domain.model.Subreddit
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.*
import com.yourparkingspace.reddit.ui.common.composables.*
import com.yourparkingspace.reddit.ui.common.utils.UiState
import com.yourparkingspace.reddit.ui.nav.Screen
import com.yourparkingspace.reddit.ui.theme.AppColors
import com.yourparkingspace.reddit.ui.theme.AppTheme
import com.yourparkingspace.reddit.ui.theme.backgroundDark
import com.yourparkingspace.reddit.ui.theme.onBackgroundVariant

@Composable
fun SubscriptionsScreen(
    navController: NavController,
    model: SubscriptionsViewModel = hiltViewModel(),
) {
    val state: UiState<SubscriptionsState> by model.subreddits.collectAsState()

    var isFavoritesExpanded by remember { mutableStateOf(true) }
    var isFollowedExpanded by remember { mutableStateOf(true) }

    fun onSubredditClicked(subreddit: Subreddit) {
        navController.navigate(
            Screen.SubredditDetails.withArgs(
                subreddit.name
            )
        )
        model.addToVisited(subreddit)
    }

    AppTheme {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = { TopBar() }
        ) {
            Box(Modifier.fillMaxSize()) {
                when (val currentState = state) {
                    is UiState.Data -> {
                        val data = currentState.data
                        LazyColumn(Modifier.fillMaxSize()) {
                            if (data.recent.isNotEmpty()) {
                                item {
                                    RecentlyVisited(
                                        data.recent,
                                        onClicked = ::onSubredditClicked,
                                        onDelete = model::deleteFromVisited
                                    )
                                }
                            }

                            if (data.favorites.isNotEmpty()) {
                                item {
                                    SectionTitle(
                                        stringResource(R.string.subscriptions_favorites),
                                        !isFavoritesExpanded
                                    ) {
                                        isFavoritesExpanded = !isFavoritesExpanded
                                    }
                                }
                                if (isFavoritesExpanded) {
                                    items(data.favorites) { subreddit ->
                                        SubscriptionListItem(
                                            subreddit,
                                            onClick = { onSubredditClicked(subreddit) },
                                            onButtonClick = { model.changeFavoriteState(subreddit) }
                                        )
                                    }
                                }
                            }

                            item {
                                SectionTitle(
                                    stringResource(R.string.subscriptions_communities),
                                    !isFollowedExpanded
                                ) {
                                    isFollowedExpanded = !isFollowedExpanded
                                }
                            }
                            if (isFollowedExpanded) {
                                if (data.all.isEmpty()) {
                                    item {
                                        Column(
                                            Modifier
                                                .height(300.dp)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(stringResource(R.string.subscriptions_nothing_joined))
                                        }
                                    }
                                } else {
                                    items(data.all) { subreddit ->
                                        SubscriptionListItem(
                                            subreddit,
                                            onClick = { onSubredditClicked(subreddit) },
                                            onButtonClick = { model.changeFavoriteState(subreddit) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        ErrorScreen(error = currentState.error) {
                            model.retry()
                        }
                    }
                    is UiState.Loading -> {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    isCollapsed: Boolean,
    onClick: () -> Unit = {}
) {
    val fullText = if (isCollapsed)
        text + " " + stringResource(R.string.subscriptions_collapsed)
    else
        text

    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.backgroundDark)
            .clickable { onClick() }
            .padding(horizontal = 5.dp, vertical = 8.dp)
    ) {
        DefaultSpacer()
        Text(
            fullText.uppercase(),
            fontSize = FontsSizes.verySmall,
            color = MaterialTheme.colors.onBackgroundVariant
        )
    }
}

@Composable
private fun SubscriptionListItem(
    subreddit: Subreddit,
    onClick: () -> Unit,
    onButtonClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(232624))
            .clickable { onClick() }
            .padding(horizontal = 5.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DefaultSpacer()
        SubredditIcon(url = subreddit.icon)
        DefaultSpacer(size = 10.dp)
        Text(
            modifier = Modifier.weight(1f),
            text = subreddit.name,
            fontSize = FontsSizes.medium,
            color = MaterialTheme.colors.onBackground
        )
        DefaultSpacer()
        Icon(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { onButtonClick() },
            imageVector = if (subreddit.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
            tint = if (subreddit.isFavorite) AppColors.blue else Color.LightGray,
            contentDescription = ""
        )
    }
}
