package com.rittmann.users.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.collectAsLazyPagingItems
import com.rittmann.components.theme.AppTheme
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.users.navigation.UserNavigation

@Composable
fun UsersScreenRoot(
    navController: NavController,
    usersViewModel: UsersViewModel = hiltViewModel(),
) {
    val users = usersViewModel.fetchUsers().collectAsLazyPagingItems()

    UsersScreen(
        navController = navController,
        users = users
    )
}

@Composable
fun UsersScreen(
    navController: NavController,
    users: LazyPagingItems<UsersResult>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        items(
            lazyPagingItems = users,
        ) { user ->
            if (user != null) {
                TextBodyBold(
                    text = user.login,
                    modifier = Modifier
                        .padding(AppTheme.dimensions.paddingMedium)
                        .clickable {
                            navController.navigate(
                                UserNavigation.UserData.transformDestination(
                                    user.login
                                )
                            )
                        },
                )
            }

        }
    }
}