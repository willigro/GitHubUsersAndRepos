package com.rittmann.datasource.usecase.users

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rittmann.datasource.mappers.toUserRepresentation
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.throwable.UserNotFound
import com.rittmann.datasource.usecase.result.ResultUC
import com.rittmann.datasource.usecase.result.fails
import com.rittmann.datasource.usecase.result.failsThrowable
import com.rittmann.datasource.usecase.result.success
import com.rittmann.datasource.usecase.users.paging.PagingRepositories
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

interface UsersUseCase {
    fun fetchUsers(
        user: String,
        page: Int,
        perPage: Int,
    ): Flow<ResultUC<List<UserRepresentation>>>

    fun fetchUserData(user: String): Flow<ResultUC<UserRepresentation>>
    fun fetchRepositories(userLogin: String): Flow<PagingData<RepositoryResult>>
}

class UsersUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
) : UsersUseCase {

    override fun fetchUsers(
        user: String,
        page: Int,
        perPage: Int,
    ): Flow<ResultUC<List<UserRepresentation>>> = flow {
        if (user.isEmpty()) {
            success(
                usersRepository.fetchUsers(since = page, perPage = perPage).map {
                    it.toUserRepresentation()
                }
            )
        } else {
            val result = usersRepository.fetchUserData(user)

            if (result == null) {
                failsThrowable<UserNotFound>()
            } else {
                success(
                    arrayListOf(
                        result.toUserRepresentation()
                    )
                )
            }
        }
    }.catch { fails(it) }

    override fun fetchUserData(user: String): Flow<ResultUC<UserRepresentation>> = flow {
        val result = usersRepository.fetchUserData(user)

        if (result == null) {
            fails()
        } else {
            emit(ResultUC.success(result.toUserRepresentation()))
        }
    }.catch { fails(it) }

    override fun fetchRepositories(userLogin: String): Flow<PagingData<RepositoryResult>> =
        Pager(
            config = PagingConfig(
                pageSize = PagingRepositories.PAGE_SIZE,
            ),
            pagingSourceFactory = {
                PagingRepositories(usersRepository, userLogin)
            }
        ).flow
}