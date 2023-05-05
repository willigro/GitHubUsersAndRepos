package com.rittmann.datasource.repositories.users

import com.rittmann.datasource.network.api.GitHubApi
import javax.inject.Inject

interface UsersRepository

class UsersRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
) : UsersRepository