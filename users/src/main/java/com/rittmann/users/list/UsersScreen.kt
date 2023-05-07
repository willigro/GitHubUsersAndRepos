package com.rittmann.users.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.rittmann.components.R
import com.rittmann.components.lifecycle.DoOnCreate
import com.rittmann.components.theme.AppTheme
import com.rittmann.components.ui.ProgressScreen
import com.rittmann.components.ui.SearchTextField
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.components.ui.TextH1
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.throwable.UserNotFound
import com.rittmann.users.navigation.UserNavigation
import java.io.IOException
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UsersScreenRoot(
    navController: NavController,
    usersViewModel: UsersViewModel = hiltViewModel(),
) {
    DoOnCreate {
        usersViewModel.fetchUsers("")
    }

    UsersScreen(
        navController = navController,
        usersState = usersViewModel.users,
        pagingUiState = usersViewModel.pagingUiState,
        fetchUser = usersViewModel::fetchUsers,
    )
}

@Composable
fun UsersScreen(
    navController: NavController,
    usersState: StateFlow<List<UserRepresentation>>,
    pagingUiState: PagingUiState,
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

        ListingUserContainer(
            modifierErrorContainer = Modifier.constrainAs(errorContainer) {
                top.linkTo(searchBarContainer.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            modifierListing = Modifier.constrainAs(listContainer) {
                top.linkTo(errorContainer.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(loadingMoreContainer.top)

                height = Dimension.fillToConstraints
            },
            modifierLoading = Modifier.constrainAs(loadingMoreContainer) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            navController = navController,
            usersState = usersState,
            pagingUiState = pagingUiState,
            fetchUser = fetchUser,
        )
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun ListingUserContainer(
    modifierErrorContainer: Modifier,
    modifierListing: Modifier,
    modifierLoading: Modifier,
    navController: NavController,
    usersState: StateFlow<List<UserRepresentation>>,
    pagingUiState: PagingUiState,
    fetchUser: (String) -> Unit,
) {
    val listState = pagingUiState.listState

    ListingUsersError(
        modifier = modifierErrorContainer,
        listState = listState,
        fetchUser = fetchUser,
    )

    val lazyColumnListState = rememberLazyListState()

    val shouldStartPaginate = remember {
        derivedStateOf {
            pagingUiState.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }

    val users = usersState.collectAsState().value

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && listState is ListState.IDLE) {
            fetchUser("")
        }
    }

    LazyColumn(
        state = lazyColumnListState,
        modifier = modifierListing
    ) {
        items(
            users
        ) { user ->
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

    ListingUsersProgress(
        modifier = modifierLoading,
        listState = listState,
    )
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
    listState: ListState,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (listState is ListState.PAGINATING) {
            CircularProgressIndicator(
                modifier = Modifier.padding(AppTheme.dimensions.progressAppendingItems),
                color = AppTheme.colors.primary,
            )
        }
    }

    if (listState is ListState.LOADING) {
        ProgressScreen(modifier = Modifier)
    }
}

@Composable
fun ListingUsersError(
    modifier: Modifier,
    listState: ListState,
    fetchUser: (String) -> Unit,
) {
    Box(modifier = modifier) {
        if (listState is ListState.ERROR) {
            when (val error = listState.throwable) {
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
                is IOException -> {
                    error.printStackTrace()

                    TextH1(
                        text = stringResource(id = R.string.tap_to_retry),
                        modifier = Modifier
                            .padding(AppTheme.dimensions.paddingSmall)
                            // TODO remove COLOR reference and use AppTheme (whole app)
                            .background(Color.Red)
                            .fillMaxWidth()
                            .clickable { fetchUser("") }
                    )
                }
            }
        }
    }
}
