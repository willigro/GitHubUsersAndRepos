package com.rittmann.datasource.usecase.di

import com.rittmann.datasource.repositories.users.UsersRepository
import com.rittmann.datasource.usecase.users.UsersUseCase
import com.rittmann.datasource.usecase.users.UsersUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providesWeatherUseCase(
        usersRepository: UsersRepository,
    ): UsersUseCase = UsersUseCaseImpl(
        usersRepository,
    )
}