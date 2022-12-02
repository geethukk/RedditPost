package com.yourparkingspace.reddit.ui.feature_posts.post_list

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yourparkingspace.domain.model.PostSortMode
import com.yourparkingspace.domain.model.PostSortMode.*
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.composables.GenericItemSelector
import com.yourparkingspace.reddit.ui.common.composables.SelectorItem

@Composable
fun PostSortModeSelector(
    selectedMode: PostSortMode,
    onSelect: (PostSortMode) -> Unit
) {
    val values = values()
    GenericItemSelector(
        items = values.map {
            SelectorItem(
                name = stringResource(it.displayNameResId),
                icon = it.icon
            )
        },
        selectedIndex = values.indexOfFirst { it == selectedMode },
        selectorText = stringResource(
            R.string.posts_sorted_by,
            stringResource(selectedMode.displayNameResId)
        ),
        dialogText = stringResource(R.string.posts_sort_by),
        onSelect = { index -> onSelect(values[index]) }
    )
}

private val PostSortMode.icon: ImageVector
    get() {
        return when (this) {
            HOT -> Icons.Outlined.Fireplace
        }
    }

private val PostSortMode.displayNameResId: Int
    get() {
        return when (this) {
            HOT -> R.string.sort_mode_hot
        }
    }

@Composable
@Preview
fun SortPreviews() {
    Column {
        for (sort in values())
            PostSortModeSelector(selectedMode = sort) {}
    }
}