package com.rittmann.datasource.usecase.users.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rittmann.datasource.mappers.toUserRepresentation
import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.throwable.UserNotFound


class PagingUsers(
    private val user: String,
    private val usersRepository: UsersRepository,
) : PagingSource<Int, UserRepresentation>() {

    companion object {
        const val PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<Int, UserRepresentation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserRepresentation> {
        return try {
            val page = params.key ?: 0
            if (user.isEmpty()) {
                val response = usersRepository.fetchUsers(since = page, perPage = PAGE_SIZE).map {
                    it.toUserRepresentation()
                }

                LoadResult.Page(
                    data = response,
                    prevKey = if (page == 0) null else page.minus(PAGE_SIZE),
                    nextKey = if (response.isEmpty()) null else page.plus(PAGE_SIZE),
                )
            } else {
                val user = usersRepository.fetchUserData(user)

                if (user == null) {
                    return LoadResult.Error(UserNotFound())
                } else {
                    val response = arrayListOf(
                        user.toUserRepresentation()
                    )

                    LoadResult.Page(
                        data = response,
                        prevKey = null,
                        nextKey = null,
                    )
                }
            }
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