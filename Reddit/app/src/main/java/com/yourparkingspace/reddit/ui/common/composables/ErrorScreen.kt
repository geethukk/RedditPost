package com.yourparkingspace.reddit.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourparkingspace.common_utils.AppError
import com.yourparkingspace.reddit.R
import com.yourparkingspace.reddit.ui.common.utils.messageResId
import com.yourparkingspace.reddit.ui.theme.backgroundDark

@Composable
fun ErrorScreen(error: AppError, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(error.messageResId),
            color = MaterialTheme.colors.onBackground
        )
        if (error != AppError.Unauthenticated) {
            DefaultSpacer(size = 10.dp)
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.primary),
                onClick = onRetry
            ) {
                Text(
                    text = stringResource(R.string.error_retry),
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 16.sp),
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}
