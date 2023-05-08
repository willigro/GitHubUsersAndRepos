package com.rittmann.githubuserscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.rittmann.components.theme.GitHubTheme
import com.rittmann.users.navigation.UserNavigation
import com.rittmann.users.navigation.userGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubTheme {
                NavigationGraph(rememberNavController())
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = UserNavigation.ROUTE) {
        navigation(
            startDestination = UserNavigation.Users.destination,
            route = UserNavigation.ROUTE,
        ) {
            userGraph(navController)
        }
    }
}
