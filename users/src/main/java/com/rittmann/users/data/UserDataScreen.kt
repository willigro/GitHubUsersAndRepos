package com.rittmann.users.data

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import coil.compose.AsyncImage
import com.rittmann.components.R
import com.rittmann.components.lifecycle.DoOnCreate
import com.rittmann.components.theme.AppTheme
import com.rittmann.components.ui.TextH1
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.network.data.RepositoryResult
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

// TODO refactor it
@Composable
fun UserDataScreen(
    userDataState: StateFlow<UserRepresentation?>,
    reposState: StateFlow<PagingData<RepositoryResult>>,
) {
    val userData = userDataState.collectAsState().value

    userData?.name?.also { name ->
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.paddingScreen)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        modifier = Modifier.size(AppTheme.dimensions.iconSizeLarge),
                        model = userData.avatarUrl,
                        placeholder = painterResource(id = R.drawable.placeholder),
                        contentDescription = null,
                    )
                }

                TextH1(
                    text = name,
                    modifier = Modifier.padding(top = AppTheme.dimensions.paddingTopBetweenComponentsLarge),
                )

                UserReposList(
                    modifier = Modifier.padding(top = AppTheme.dimensions.paddingTopBetweenComponentsLarge),
                    reposState = reposState
                )
            }
    }
}