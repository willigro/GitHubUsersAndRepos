package com.rittmann.datasource.network.api

import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.datasource.network.config.CONTENT_TYPE_JSON
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UserDataResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @Headers(CONTENT_TYPE_JSON)
    @GET("users")
    suspend fun fetchUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<UsersResult>>

    @Headers(CONTENT_TYPE_JSON)
    @GET("users/{user}")
    suspend fun fetchUserData(
        @Path("user") user: String,
    ): Response<UserDataResult>

    @Headers(CONTENT_TYPE_JSON)
    @GET("users/{user}/repos")
    suspend fun fetchUserRepos(
        @Path("user") user: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<RepositoryResult>>
}