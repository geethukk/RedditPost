package com.yourparkingspace.reddit.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yourparkingspace.domain.model.FeedType
import com.yourparkingspace.reddit.ui.feature_posts.post_list.PostsScreen
import com.yourparkingspace.reddit.ui.feature_profile.ProfileScreen
import com.yourparkingspace.reddit.ui.feature_subscriptions.SubscriptionsScreen
import com.yourparkingspace.reddit.ui.theme.inputBackground

@Composable
fun BottomNavScreen(mainController: NavController) {
    val items = listOf(Screen.Home, Screen.Subscriptions, Screen.Profile)
    val navController = rememberNavController()
    Scaffold(
        Modifier
            //.padding(vertical = 50.dp)
            .background(Color.Black),

        bottomBar = {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colors.inputBackground)
                ) { }
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            modifier = Modifier.background(MaterialTheme.colors.background),
                            selectedContentColor = Color(0xFF4b77de),
                            unselectedContentColor = MaterialTheme.colors.onBackground,
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(stringResource(screen.titleResId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                Modifier
                    .padding(innerPadding)
                    .background(Color.Black)
            ) {
                composable(Screen.Home.route) {
                    PostsScreen(
                        feedType = FeedType.Popular,
                        navController = mainController
                    )
                }
                composable(Screen.Subscriptions.route) {
                    SubscriptionsScreen(mainController)
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController = mainController)
                }
            }
        }
    }
}

private val Screen.icon: ImageVector
    get() {
        return when (this) {
            Screen.Home -> Icons.Filled.Home
            Screen.Profile -> Icons.Filled.Person
            Screen.Subscriptions -> Icons.Filled.Subscriptions
            else -> Icons.Filled.Home
        }
    }