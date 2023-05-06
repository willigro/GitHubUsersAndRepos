package com.rittmann.datasource.usecase.users

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rittmann.datasource.network.data.UsersResult
import com.rittmann.datasource.repositories.users.UsersRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface UsersUseCase {
    fun fetchUsers(): Flow<PagingData<UsersResult>>
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
}

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