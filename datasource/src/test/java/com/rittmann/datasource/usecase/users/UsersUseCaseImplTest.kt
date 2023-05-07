package com.rittmann.datasource.usecase.users

import com.rittmann.datasource.mock.mockUserDataResult
import com.rittmann.datasource.mock.mockUsers
import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.throwable.UserNotFound
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UsersUseCaseImplTest {

    private lateinit var userCase: UsersUseCaseImpl
    private lateinit var usersRepository: UsersRepository

    @Before
    fun setup() {
        usersRepository = mockk()

        userCase = UsersUseCaseImpl(
            usersRepository = usersRepository,
        )
    }

    @Test
    fun `fetchUsers, try loading users without query, load 10 users`() = runTest {
        val perPage = 10
        val page = 1

        val mockResult = mockUsers(perPage)

        coEvery { usersRepository.fetchUsers(since = page, perPage = perPage) } returns mockResult

        val users = userCase.fetchUsers(
            user = "",
            page = page,
            perPage = perPage,
        ).first()

        assertTrue(users.isSuccess)
        assertThat(users.getOrNull()!!.size, `is`(10))
    }

    @Test
    fun `fetchUsers, try loading users without query, throws IOException`() = runTest {
        val perPage = 10
        val page = 1

        coEvery { usersRepository.fetchUsers(since = page, perPage = perPage) } throws IOException()

        val users = userCase.fetchUsers(
            user = "",
            page = page,
            perPage = perPage,
        ).first()

        assertTrue(users.isFailure)
        assertThat(users.getError(), instanceOf(IOException::class.java))
    }

    @Test
    fun `fetchUsers, try loading users with query, loading the user`() = runTest {
        val perPage = 10
        val page = 1
        val user = "test"

        coEvery { usersRepository.fetchUserData(user = user) } returns mockUserDataResult

        val users = userCase.fetchUsers(
            user = user,
            page = page,
            perPage = perPage,
        ).first()

        assertTrue(users.isSuccess)
        assertThat(users.getOrNull()!!.size, `is`(1))
    }

    @Test
    fun `fetchUsers, try loading users with query, user is not found, throws UserNotFound`() = runTest {
        val perPage = 10
        val page = 1
        val user = "test"

        coEvery { usersRepository.fetchUserData(user = user) } returns null

        val users = userCase.fetchUsers(
            user = user,
            page = page,
            perPage = perPage,
        ).first()

        assertTrue(users.isFailure)
        assertThat(users.getError(), instanceOf(UserNotFound::class.java))
    }
}