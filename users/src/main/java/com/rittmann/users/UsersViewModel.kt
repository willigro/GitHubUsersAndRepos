package com.rittmann.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.datasource.usecase.users.UsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
) : ViewModel() {

    // TODO: map userResult to user(ui)
    fun fetchUsers(): Flow<PagingData<UsersResult>> =
        usersUseCase.fetchUsers().cachedIn(viewModelScope)
}