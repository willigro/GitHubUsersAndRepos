package com.rittmann.users.data

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rittmann.components.lifecycle.DoOnCreate
import com.rittmann.components.ui.TextH1
import com.rittmann.datasource.network.data.UserDataResult
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
        userDataState = userDataViewModel.userData
    )
}

@Composable
fun UserDataScreen(userDataState: StateFlow<UserDataResult?>) {
    val userData = userDataState.collectAsState().value

    if (userData != null) {
        TextH1(
            text = userData.name,
            modifier = Modifier.background(Color.Red),
        )
    }
}