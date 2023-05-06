package com.rittmann.users.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rittmann.users.data.UserDataScreenRoot
import com.rittmann.users.list.UsersScreenRoot


fun NavGraphBuilder.userGraph(navController: NavController) {
    composable(UserNavigation.Users.destination) {
        UsersScreenRoot(navController = navController)
    }

    composable(UserNavigation.UserData.destination) { backStackEntry ->
        UserDataScreenRoot(
            navController = navController,
            user = backStackEntry.arguments?.getString(UserNavigation.UserData.KEY).orEmpty(),
        )
    }
}
