package com.rittmann.users.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.usecase.users.UsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
) : ViewModel() {

    private val _users: MutableStateFlow<PagingData<UserRepresentation>> =
        MutableStateFlow(PagingData.empty())
    val users: StateFlow<PagingData<UserRepresentation>>
        get() = _users

    fun fetchUsers(name: String) = viewModelScope.launch {
        usersUseCase.fetchUsers(name).collectLatest {
            _users.value = it
        }
    }
}