package com.rittmann.users.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.rittmann.components.R
import com.rittmann.components.lifecycle.DoOnCreate
import com.rittmann.components.theme.AppTheme
import com.rittmann.components.ui.ProgressScreen
import com.rittmann.components.ui.SearchTextField
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.components.ui.TextH1
import com.rittmann.components.ui.isError
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.throwable.UserNotFound
import com.rittmann.users.navigation.UserNavigation

@Composable
fun UsersScreenRoot(
    navController: NavController,
    usersViewModel: UsersViewModel = hiltViewModel(),
) {
    DoOnCreate {
        usersViewModel.fetchUsers("")
    }

    val users = usersViewModel.users.collectAsLazyPagingItems()

    UsersScreen(
        navController = navController,
        usersPaging = users,
        fetchUser = usersViewModel::fetchUsers,
    )
}

@Composable
fun UsersScreen(
    navController: NavController,
    usersPaging: LazyPagingItems<UserRepresentation>,
    fetchUser: (String) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val (
            searchBarContainer,
            errorContainer,
            listContainer,
            loadingMoreContainer,
        ) = createRefs()

        UsersSearchBar(
            modifier = Modifier.constrainAs(searchBarContainer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fetchUser = fetchUser,
        )

        Box(
            modifier = Modifier.constrainAs(errorContainer) {
                top.linkTo(searchBarContainer.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            ListingUsersError(users = usersPaging)
        }

        LazyColumn(
            modifier = Modifier.constrainAs(listContainer) {
                top.linkTo(errorContainer.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(loadingMoreContainer.top)

                height = Dimension.fillToConstraints
            }
        ) {
            items(
                lazyPagingItems = usersPaging,
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

        ListingUsersProgress(
            modifier = Modifier.constrainAs(loadingMoreContainer) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            users = usersPaging,
        )
    }
}

@Composable
fun UsersSearchBar(
    modifier: Modifier,
    fetchUser: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    SearchTextField(
        modifier = modifier,
        text = name,
        hint = stringResource(id = R.string.user_list_search_bar_hint),
        onTextChanged = { name = it },
        fetch = fetchUser,
    )
}

@Composable
fun ListingUsersProgress(
    modifier: Modifier,
    users: LazyPagingItems<UserRepresentation>,
) {
    val loadState = users.loadState

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (loadState.append == LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(AppTheme.dimensions.progressAppendingItems),
                color = AppTheme.colors.primary,
            )
        }
    }

    if (loadState.refresh == LoadState.Loading) {
        ProgressScreen(modifier = Modifier)
    }
}

@Composable
fun ListingUsersError(users: LazyPagingItems<UserRepresentation>) {
    when (val error = users.loadState.isError()) {
        is UserNotFound -> {
            error.printStackTrace()

            TextH1(
                text = stringResource(id = R.string.user_list_user_not_found),
                modifier = Modifier
                    .padding(AppTheme.dimensions.paddingSmall)
                    // TODO remove COLOR reference and use AppTheme (whole app)
                    .background(Color.Blue)
                    .fillMaxWidth()
            )
        }
        is Throwable -> {
            error.printStackTrace()

            TextH1(
                text = stringResource(id = R.string.tap_to_retry),
                modifier = Modifier
                    .padding(AppTheme.dimensions.paddingSmall)
                    // TODO remove COLOR reference and use AppTheme (whole app)
                    .background(Color.Red)
                    .fillMaxWidth()
                    .clickable { users.retry() }
            )
        }
    }
}
