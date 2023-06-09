package com.rittmann.datasource.mappers

import com.rittmann.datasource.models.UserRepresentation
import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.datasource.network.data.UsersResult

fun UsersResult.toUserRepresentation() =
    UserRepresentation(login = login, avatarUrl = avatarURL, name = null)

fun UserDataResult.toUserRepresentation() =
    UserRepresentation(login = login, avatarUrl = avatarURL, name = name)