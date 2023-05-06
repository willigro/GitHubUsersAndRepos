package com.rittmann.datasource.repositories.users

import com.rittmann.datasource.network.api.GitHubApi
import com.rittmann.datasource.network.data.UsersResult
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UsersRepository {
    suspend fun fetchUsers(since: Int, perPage: Int): List<UsersResult>
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
}