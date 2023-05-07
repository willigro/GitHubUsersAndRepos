package com.rittmann.users.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var lastName = ""
    val pagingUiState = PagingUiState()

    private val _users: MutableStateFlow<ArrayList<UserRepresentation>> =
        MutableStateFlow(arrayListOf())
    val users: StateFlow<List<UserRepresentation>>
        get() = _users

    fun fetchUsers(name: String) = viewModelScope.launch {
        if (name != lastName) {
            pagingUiState.resetPaging()
        }

        lastName = name

        if (pagingUiState.canLoadData()) {
            pagingUiState.configureLoadingState()

            usersUseCase.fetchUsers(name, pagingUiState.page, PagingUiState.PAGE_SIZE)
                .collectLatest { usersResult ->
                    val result = usersResult.getOrNull()

                    if (result.isNullOrEmpty()) {
                        val error = usersResult.getError()

                        pagingUiState.listState = if (error == null) {
                            ListState.EXHAUST
                        } else {
                            ListState.ERROR(error)
                        }
                    } else {
                        pagingUiState.configureCanPaginate(result.size)

                        if (pagingUiState.isInitialPage()) {
                            _users.value.clear()
                        }

                        val list = arrayListOf<UserRepresentation>().apply {
                            addAll(_users.value)
                            addAll(result)
                        }

                        _users.value = list

                        pagingUiState.isIdle()

                        pagingUiState.goNextIfCan()
                    }
                }
        }
    }
}

class PagingUiState(
    private val initialPage: Int = 0,
) {
    companion object {
        const val PAGE_SIZE = 40
    }

    var page by mutableStateOf(initialPage)
    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf<ListState>(ListState.IDLE)

    fun configureLoadingState() {
        listState = if (page == initialPage) ListState.LOADING else ListState.PAGINATING
    }

    fun canLoadData(): Boolean = page == initialPage
            || (page != initialPage && canPaginate)
            && (listState is ListState.IDLE || listState is ListState.ERROR)

    fun resetPaging() {
        page = initialPage
    }

    fun isInitialPage() = page == initialPage

    fun configureCanPaginate(size: Int) {
        canPaginate = size == PAGE_SIZE
    }

    fun isIdle() {
        listState = ListState.IDLE
    }

    fun goNextIfCan() {
        if (canPaginate) {
            nextPage()
        }
    }

    private fun nextPage() {
        page = page.plus(1)
    }
}

sealed class ListState {
    object IDLE : ListState()
    object LOADING : ListState()
    object PAGINATING : ListState()
    object EXHAUST : ListState()
    class ERROR(
        val throwable: Throwable
    ) : ListState()
}