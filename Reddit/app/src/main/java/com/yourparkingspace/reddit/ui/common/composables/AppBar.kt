package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourparkingspace.reddit.ui.theme.inputBackground
import com.yourparkingspace.reddit.ui.theme.isDark
import com.yourparkingspace.reddit.ui.theme.onInputBackground

@Composable
@Preview
fun TopBar(modifier: Modifier = Modifier, transparent: Boolean = false) {
    Row(
        modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                MaterialTheme.colors.background.copy(alpha = if (transparent) 0.6f else 1.0f)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "",
                tint = if (isDark.value) Color.White else Color.Black
            )
        }
        SearchView(Modifier.weight(1f))
        IconButton(onClick = { isDark.value = !isDark.value }) {
            val icon = if (isDark.value) Icons.Filled.DarkMode else Icons.Filled.LightMode
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}

@Composable
fun SearchView(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier
            .height(30.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colors.inputBackground)
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 4.dp),
            imageVector = Icons.Filled.Search,
            contentDescription = "",
            tint = MaterialTheme.colors.onBackground
        )
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .background(color = MaterialTheme.colors.inputBackground),
            value = query,
            textStyle = TextStyle.Default.copy(color = MaterialTheme.colors.onInputBackground),
            onValueChange = { query = it },
            cursorBrush = SolidColor(MaterialTheme.colors.primary)
        )
    }
}