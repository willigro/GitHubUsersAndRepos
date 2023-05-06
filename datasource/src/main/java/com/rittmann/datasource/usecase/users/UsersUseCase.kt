package com.rittmann.datasource.usecase.users

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.usecase.result.ResultUC
import com.rittmann.datasource.usecase.result.fails
import com.rittmann.datasource.usecase.users.paging.PagingRepositories
import com.rittmann.datasource.usecase.users.paging.PagingUsers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UsersUseCase {
    fun fetchUsers(): Flow<PagingData<UsersResult>>
    fun fetchUserData(user: String): Flow<ResultUC<UserDataResult>>
    fun fetchRepositories(user: UserDataResult): Flow<PagingData<RepositoryResult>>
}

class UsersUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
) : UsersUseCase {

    override fun fetchUsers(): Flow<PagingData<UsersResult>> = Pager(
        config = PagingConfig(
            pageSize = PagingUsers.PAGE_SIZE,
        ),
        pagingSourceFactory = {
            PagingUsers(usersRepository)
        }
    ).flow

    override fun fetchUserData(user: String): Flow<ResultUC<UserDataResult>> = flow {
        val result = usersRepository.fetchUserData(user)

        if (result == null) {
            fails()
        } else {
            emit(ResultUC.success(result))
        }
    }

    override fun fetchRepositories(user: UserDataResult): Flow<PagingData<RepositoryResult>> =
        Pager(
            config = PagingConfig(
                pageSize = PagingUsers.PAGE_SIZE,
            ),
            pagingSourceFactory = {
                PagingRepositories(usersRepository, user.login)
            }
        ).flow
}