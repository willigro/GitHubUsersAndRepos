package com.rittmann.datasource.usecase.users.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.datasource.repositories.users.UsersRepository


class PagingUsers(
    private val usersRepository: UsersRepository,
) : PagingSource<Int, UsersResult>() {

    companion object {
        const val PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<Int, UsersResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersResult> {
        return try {
            val page = params.key ?: 0
            val response = usersRepository.fetchUsers(since = page, perPage = PAGE_SIZE)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page.minus(PAGE_SIZE),
                nextKey = if (response.isEmpty()) null else page.plus(PAGE_SIZE),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class PagingRepositories(
    private val usersRepository: UsersRepository,
    private val user: String,
) : PagingSource<Int, RepositoryResult>() {

    companion object {
        const val PAGE_SIZE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryResult> {
        return try {
            val page = params.key ?: 0
            val response = usersRepository.fetchRepositories(
                user = user,
                page = page,
                perPage = PAGE_SIZE,
            )

            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}