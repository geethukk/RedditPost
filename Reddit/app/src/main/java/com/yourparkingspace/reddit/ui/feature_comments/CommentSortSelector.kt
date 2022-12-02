package com.yourparkingspace.reddit.ui.feature_comments

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.yourparkingspace.domain.model.CommentSortMode

@Composable
fun CommentSortModeSelector(
    selectedMode: CommentSortMode,
    onSelect: (CommentSortMode) -> Unit
) {
/*    val values = CommentSortMode.values()
    GenericItemSelector(
        items = values.map {
            SelectorItem(
                name = stringResource(it.displayNameResId),
                icon = it.icon
            )
        },
        selectedIndex = values.indexOfFirst { it == selectedMode },
        selectorText = stringResource(
            R.string.comments_sorted_by,
            stringResource(selectedMode.displayNameResId)
        ),
        dialogText = stringResource(R.string.comments_sort_by),
        onSelect = { index -> onSelect(values[index]) }
    )*/
}

private val CommentSortMode.icon: ImageVector
    get() {
        return when (this) {
            CommentSortMode.BEST -> Icons.Outlined.Favorite
            CommentSortMode.NEW -> Icons.Outlined.NewReleases
            CommentSortMode.TOP -> Icons.Outlined.BarChart
            CommentSortMode.CONTROVERSIAL -> Icons.Outlined.Dangerous
            CommentSortMode.OLD -> Icons.Outlined.Timer
            CommentSortMode.QA -> Icons.Outlined.QuestionAnswer
        }
    }

/*private val CommentSortMode.displayNameResId: Int
    get() {
        return when (this) {

            else -> {}
        }
    }*/

@Composable
@Preview
fun SortPreviews() {
    Column {
        for (sort in CommentSortMode.values())
            CommentSortModeSelector(selectedMode = sort) {}
    }
}