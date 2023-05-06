package com.rittmann.users.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.users.UserNavigation
import com.rittmann.users.UsersScreenRoot


fun NavGraphBuilder.userGraph(navController: NavController) {
    composable(UserNavigation.Users.destination) {
        UsersScreenRoot(navController = navController)
    }

    composable(UserNavigation.UserData.destination) { backStackEntry ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        ) {
            TextBodyBold(
                text = backStackEntry.arguments?.getString(UserNavigation.UserData.KEY).orEmpty()
            )
        }
    }
}
