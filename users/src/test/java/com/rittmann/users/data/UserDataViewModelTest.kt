package com.rittmann.users.data

import androidx.paging.PagingData
import app.cash.turbine.test
import com.rittmann.datasource.network.data.RepositoryResult
import com.rittmann.datasource.test.dispatcher.MainCoroutineRule
import com.rittmann.datasource.test.mock.mockUserDataResult
import com.rittmann.datasource.usecase.result.ResultUC
import com.rittmann.datasource.usecase.users.UsersUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class UserDataViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: UserDataViewModel
    private lateinit var usersUseCase: UsersUseCase

    @Before
    fun setup() {
        usersUseCase = mockk()

        viewModel = UserDataViewModel(
            dispatcherProvider = mainCoroutineRule.testDispatcher,
            usersUseCase = usersUseCase,
        )
    }

    @Test
    fun `fetchUserData, returns null from UseCase, do not call fetchRepositories`() = runTest {
        every { usersUseCase.fetchUserData("") } returns flowOf(ResultUC.success(null))

        viewModel.fetchUserData("")

        viewModel.userData.test {
            assertThat(null, `is`(awaitItem()))
            cancelAndIgnoreRemainingEvents()
        }

        verify { usersUseCase.fetchUserData("") }
        verify(exactly = 0) { usersUseCase.fetchRepositories(any()) }
    }

    @Test
    fun `fetchUserData, returns a user from UseCase, call fetchRepositories`() = runTest {
        val user = ""
        val repos = arrayListOf<RepositoryResult>()
        val pagingRepos = PagingData.from(repos)

        every {
            usersUseCase.fetchUserData(user)
        } returns flowOf(
            ResultUC.success(mockUserDataResult)
        )

        every {
            usersUseCase.fetchRepositories(mockUserDataResult)
        } returns flowOf(
            pagingRepos
        )

        viewModel.fetchUserData(user)

        viewModel.userData.test {
            assertThat(mockUserDataResult, `is`(awaitItem()))
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.repos.test {
            assertThat(pagingRepos, `is`(awaitItem()))
            cancelAndIgnoreRemainingEvents()
        }

        verify { usersUseCase.fetchUserData(user) }
        verify { usersUseCase.fetchRepositories(mockUserDataResult) }
    }

    @Test
    fun `fetchUserData, UseCase fetchUserData returns IOException`() = runTest {
        val user = ""

        every {
            usersUseCase.fetchUserData(user)
        } throws IOException()

        viewModel.fetchUserData(user)

        viewModel.userData.test {
            assertThat(null, `is`(awaitItem()))
            cancelAndIgnoreRemainingEvents()
        }

        verify { usersUseCase.fetchUserData(user) }
        verify(exactly = 0) { usersUseCase.fetchRepositories(any()) }
    }

    @Test
    fun `fetchUserData, returns user, but UseCase fetchRepositories returns IOException`() =
        runTest {
            val user = ""

            every {
                usersUseCase.fetchUserData(user)
            } returns flowOf(
                ResultUC.success(mockUserDataResult)
            )

            every {
                usersUseCase.fetchRepositories(mockUserDataResult)
            } throws IOException()

            viewModel.fetchUserData(user)

            viewModel.userData.test {
                assertThat(mockUserDataResult, `is`(awaitItem()))
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.repos.test {
                assertThat(PagingData.empty(), `is`(awaitItem()))
                cancelAndIgnoreRemainingEvents()
            }

            verify { usersUseCase.fetchUserData(user) }
            verify { usersUseCase.fetchRepositories(mockUserDataResult) }
        }
}