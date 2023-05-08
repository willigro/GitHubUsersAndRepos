package com.rittmann.datasource.repositories.users

import com.rittmann.datasource.lifecycle.DispatcherProvider
import com.rittmann.datasource.network.api.GitHubApi
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.datasource.network.data.UsersResult
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface UsersRepository {
    suspend fun fetchUsers(since: Int, perPage: Int): List<UsersResult>
    suspend fun fetchUserData(user: String): UserDataResult?
    suspend fun fetchRepositories(user: String, page: Int, perPage: Int): List<RepositoryResult>
}

class UsersRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val gitHubApi: GitHubApi,
) : UsersRepository {

    override suspend fun fetchUsers(
        since: Int,
        perPage: Int,
    ): List<UsersResult> = withContext(dispatcherProvider.io) {
        gitHubApi.fetchUsers(since, perPage).body().orEmpty()
    }

    override suspend fun fetchUserData(
        user: String,
    ): UserDataResult? = withContext(dispatcherProvider.io) {
        gitHubApi.fetchUserData(user).body()
    }

    override suspend fun fetchRepositories(
        user: String,
        page: Int,
        perPage: Int,
    ): List<RepositoryResult> = withContext(dispatcherProvider.io) {
        gitHubApi.fetchUserRepos(user, page, perPage).body().orEmpty()
    }
}