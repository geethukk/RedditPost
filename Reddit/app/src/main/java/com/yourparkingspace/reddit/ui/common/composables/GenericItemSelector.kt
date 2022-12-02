package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yourparkingspace.reddit.ui.common.FontsSizes
import com.yourparkingspace.reddit.ui.theme.AppColors
import com.yourparkingspace.reddit.ui.theme.backgroundDark

data class SelectorItem(
    val name: String,
    val icon: ImageVector
)

@Composable
fun GenericItemSelector(
    items: List<SelectorItem>,
    selectedIndex: Int,
    selectorText: String,
    dialogText: String,
    onSelect: (Int) -> Unit
) {
    var dialogShown by remember { mutableStateOf(false) }
    val selectedItem = items[selectedIndex]

    Box {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.backgroundDark)
            ) {
                Row(
                    Modifier
                        .clickable { dialogShown = true }
                        .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = selectedItem.icon,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                    DefaultSpacer()
                    Text(
                        text = selectorText.uppercase(),
                        fontSize = FontsSizes.small,
                        color = Color.Gray,
                        modifier = Modifier.wrapContentWidth()
                    )
                    DefaultSpacer()
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = "Select",
                        tint = Color.Gray
                    )
                }
            }
        }

        if (dialogShown) {
            SortSelectorDialog(
                modifier = Modifier.align(Alignment.BottomCenter),
                items = items,
                selectedIndex = selectedIndex,
                title = dialogText,
                onDismiss = { dialogShown = false },
                onSortSelected = {
                    dialogShown = false
                    onSelect(it)
                }
            )
        }
    }
}

@Composable
private fun SelectorDialogListItem(
    item: SelectorItem,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = "",
            tint = if (isCurrent) MaterialTheme.colors.onBackground else Color.Gray
        )
        DefaultSpacer(size = 16.dp)
        Text(
            text = item.name,
            color = if (isCurrent) MaterialTheme.colors.onBackground else Color.Gray
        )
        DefaultSpacer()
        if (isCurrent)
            Row(
                Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    tint = AppColors.blue
                )
            }
    }
}

@Composable
private fun SortSelectorDialog(
    modifier: Modifier = Modifier,
    items: List<SelectorItem>,
    selectedIndex: Int,
    title: String,
    onSortSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(
                title.uppercase(),
                fontSize = FontsSizes.small,
                color = Color.Gray,
                modifier = Modifier
                    .offset(x = (-5).dp)
                    .padding(bottom = 10.dp)
            )
        },
        onDismissRequest = { onDismiss() },
        backgroundColor = MaterialTheme.colors.background,
        buttons = {
            Column(Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    SelectorDialogListItem(item = item, isCurrent = index == selectedIndex) {
                        onSortSelected(index)
                    }
                }
            }
        }
    )
}