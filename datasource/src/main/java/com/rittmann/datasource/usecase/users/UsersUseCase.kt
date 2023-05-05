package com.rittmann.datasource.usecase.users

import com.rittmann.datasource.repositories.users.UsersRepository
import javax.inject.Inject

interface UsersUseCase

class UsersUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
) : UsersUseCase