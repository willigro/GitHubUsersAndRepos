package com.rittmann.users.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import com.rittmann.components.lifecycle.DoOnCreate
import com.rittmann.components.ui.TextH1
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.users.repos.UserReposList
import kotlinx.coroutines.flow.StateFlow


@Composable
fun UserDataScreenRoot(
    navController: NavController,
    user: String,
    userDataViewModel: UserDataViewModel = hiltViewModel(),
) {

    DoOnCreate {
        userDataViewModel.fetchUserData(user)
    }

    UserDataScreen(
        userDataState = userDataViewModel.userData,
        reposState = userDataViewModel.repos,
    )
}

@Composable
fun UserDataScreen(
    userDataState: StateFlow<UserDataResult?>,
    reposState: StateFlow<PagingData<RepositoryResult>>,
) {
    val userData = userDataState.collectAsState().value

    userData?.name?.also { name ->
        Column {
            TextH1(
                text = name,
                modifier = Modifier.background(Color.Red),
            )

            UserReposList(
                modifier = Modifier,
                reposState = reposState
            )
        }
    }
}