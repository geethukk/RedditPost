package com.yourparkingspace.reddit.ui.feature_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourparkingspace.reddit.ui.common.composables.TopBar
import com.yourparkingspace.reddit.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    model: ProfileViewModel = hiltViewModel()
) {
    AppTheme {
        Column {
            TopBar()
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Nothing to see here")
            }
        }
    }
}