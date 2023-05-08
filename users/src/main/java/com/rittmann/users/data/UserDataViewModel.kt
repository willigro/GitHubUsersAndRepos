package com.rittmann.users.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rittmann.datasource.lifecycle.DispatcherProvider
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.usecase.users.UsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val usersUseCase: UsersUseCase,
) : ViewModel() {

    private val _userData: MutableStateFlow<UserRepresentation?> = MutableStateFlow(null)
    val userData: StateFlow<UserRepresentation?>
        get() = _userData

    private val _repos: MutableStateFlow<PagingData<RepositoryResult>> =
        MutableStateFlow(PagingData.empty())
    val repos: StateFlow<PagingData<RepositoryResult>>
        get() = _repos

    fun fetchUserData(user: String) = viewModelScope.launch {
        usersUseCase.fetchUserData(user)
            .flowOn(dispatcherProvider.io)
            .collectLatest {
                _userData.value = it.getOrNull()

                fetchRepositories()
            }
    }

    private fun fetchRepositories() = viewModelScope.launch {
        _userData.value?.also { user ->
            usersUseCase.fetchRepositories(user.login)
                .flowOn(dispatcherProvider.io)
                .collectLatest {
                    _repos.value = it
                }
        }
    }
}