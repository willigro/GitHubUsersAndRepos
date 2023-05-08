package com.rittmann.datasource.repositories.di

import com.rittmann.datasource.lifecycle.DispatcherProvider
import com.rittmann.datasource.network.api.GitHubApi
import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.repositories.users.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUsersRepository(
        dispatcherProvider: DispatcherProvider,
        gitHubApi: GitHubApi,
    ): UsersRepository = UsersRepositoryImpl(
        dispatcherProvider,
        gitHubApi,
    )
}