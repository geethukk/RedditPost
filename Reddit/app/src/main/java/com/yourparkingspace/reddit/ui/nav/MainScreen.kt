package com.yourparkingspace.reddit.ui.nav

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yourparkingspace.reddit.ui.feature_posts.post_details.PostDetailsScreen
import com.yourparkingspace.reddit.ui.feature_subreddit_details.SubredditScreen
import com.yourparkingspace.reddit.ui.theme.AppTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    AppTheme {
        NavHost(
            modifier = Modifier.background(Color.Black),
            navController = navController,
            startDestination = Screen.MainScreen.route
        ) {
            composable(Screen.MainScreen.route) { BottomNavScreen(navController) }

            composable(
                route = Screen.SubredditDetails.route + "/{name}",
                arguments = listOf(
                    navArgument("name") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    }

                )) { entry ->
                val subName = entry.arguments?.getString("name") ?: "null"
                SubredditScreen(navController, subredditName = subName)
            }

            composable(
                route = Screen.PostDetails.route + "/{postId}/{subName}",
                arguments = listOf(
                    navArgument("postId") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    },
                    navArgument("subName") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = false
                    }

                )) { entry ->
                val postId = entry.arguments?.getString("postId") ?: "null"
                val subName = entry.arguments?.getString("subName") ?: "null"
                PostDetailsScreen(navController, postId = postId, subredditName = subName)
            }
        }
    }
}