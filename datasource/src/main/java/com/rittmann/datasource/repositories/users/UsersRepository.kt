package com.rittmann.datasource.repositories.users

import com.rittmann.datasource.network.api.GitHubApi
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.datasource.network.data.UsersResult
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UsersRepository {
    suspend fun fetchUsers(since: Int, perPage: Int): List<UsersResult>
    suspend fun fetchUserData(user: String): UserDataResult?
    suspend fun fetchRepositories(user: String, page: Int, perPage: Int): List<RepositoryResult>
}

class UsersRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
) : UsersRepository {

    override suspend fun fetchUsers(
        since: Int,
        perPage: Int,
    ): List<UsersResult> = withContext(Dispatchers.IO) {
        gitHubApi.fetchUsers(since, perPage).body().orEmpty()
    }

    override suspend fun fetchUserData(
        user: String,
    ): UserDataResult? = withContext(Dispatchers.IO) {
        gitHubApi.fetchUserData(user).body()
    }

    override suspend fun fetchRepositories(
        user: String,
        page: Int,
        perPage: Int,
    ): List<RepositoryResult> = withContext(Dispatchers.IO) {
        gitHubApi.fetchUserRepos(user, page, perPage).body().orEmpty()
    }
}