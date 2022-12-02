package com.yourparkingspace.reddit.ui.feature_subreddit_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourparkingspace.domain.model.SubscriptionState
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.composables.DefaultSpacer
import com.yourparkingspace.reddit.ui.common.FontsSizes
import com.yourparkingspace.reddit.ui.common.utils.formatNumber
import com.yourparkingspace.reddit.ui.theme.AppTheme
import com.yourparkingspace.reddit.ui.theme.isDark
import com.yourparkingspace.reddit.ui.theme.onBackgroundVariant

@Composable
fun SubredditHeaderInfo(
    name: String,
    members: Int,
    online: Int,
    description: String,
    subscriptionState: SubscriptionState?,
    onJoinClicked: () -> Unit
) {
    var isDescriptionExpanded: Boolean by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6
            )
            if (subscriptionState != null) {
                JoinButton(joined = subscriptionState is SubscriptionState.Subscribed) {
                    onJoinClicked()
                }
            }
        }

        DefaultSpacer()

        Text(
            fontSize = FontsSizes.small,
            text = stringResource(
                R.string.subreddit_members_and_online,
                formatNumber(members),
                formatNumber(online)
            ),
            color = MaterialTheme.colors.onBackgroundVariant
        )

        DefaultSpacer()

        Text(
            fontSize = FontsSizes.small,
            text = description,
            maxLines = if (isDescriptionExpanded) Integer.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                isDescriptionExpanded = !isDescriptionExpanded
            }
        )
        DefaultSpacer(size = 8.dp)
    }
}

@Composable
fun JoinButton(joined: Boolean, onClick: () -> Unit) {
    if (!joined) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(24.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Join")
        }
    } else
        OutlinedButton(
            onClick = onClick,
            border = BorderStroke(1.dp, color = MaterialTheme.colors.onBackgroundVariant),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.onBackgroundVariant,
            ),
            modifier = Modifier.height(24.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                stringResource(R.string.subreddit_joined),
                fontSize = FontsSizes.verySmall,
            )
        }
}

@Composable
@Preview
private fun HeaderPreview() {
    AppTheme {
        Surface(Modifier.background(MaterialTheme.colors.background)) {
            SubredditHeaderInfo(
                name = "r/subreddit",
                members = 213213,
                online = 213,
                description = "Lorem ipsum sir Lorem ipsum sir dolomet Lorem ipsum sir dolomet Lorem ipsum sir dolomet",
                subscriptionState = SubscriptionState.Subscribed(false),
                onJoinClicked = {}
            )
        }
    }
}

@Composable
@Preview
private fun HeaderPreviewLight() {

    isDark.value = false

    AppTheme {
        Surface(Modifier.background(MaterialTheme.colors.background)) {
            SubredditHeaderInfo(
                name = "r/subreddit",
                members = 213213,
                online = 213,
                description = "Lorem ipsum sir dolomet Lorem ipsum sir dolomet Lorem ipsum sir dolomet Lorem ipsum sir dolomet",
                subscriptionState = SubscriptionState.Subscribed(false),
                onJoinClicked = {}
            )
        }
    }
}

@Composable
@Preview
private fun ButtonPreview() {
    isDark.value = true
    AppTheme {
        JoinButton(joined = true) {
        }
    }
}

@Composable
@Preview
private fun ButtonPreviewLight() {
    isDark.value = false
    AppTheme {
        JoinButton(joined = true) {
        }
    }
}