package com.rittmann.datasource.mock

import com.rittmann.datasource.network.data.UserDataResult
import com.rittmann.datasource.network.data.UsersResult


// TODO should be moved to test module
fun mockUsers(quantity: Int): ArrayList<UsersResult> {
    val arr = arrayListOf<UsersResult>()

    for (i in 0 until quantity) {
        arr.add(
            UsersResult(
                login = "",
                id = i.toLong(),
                nodeID = "",
                avatarURL = "",
                gravatarID = "",
                url = "",
                htmlURL = "",
                followersURL = "",
                followingURL = "",
                gistsURL = "",
                starredURL = "",
                subscriptionsURL = "",
                organizationsURL = "",
                reposURL = "",
                eventsURL = "",
                receivedEventsURL = "",
                type = "",
                siteAdmin = false,
            )
        )
    }

    return arr
}

val mockUserDataResult = UserDataResult(
    login = "",
    id = 0L,
    nodeID = "",
    avatarURL = "",
    gravatarID = "",
    url = "",
    htmlURL = "",
    followersURL = "",
    followingURL = "",
    gistsURL = "",
    starredURL = "",
    subscriptionsURL = "",
    organizationsURL = "",
    reposURL = "",
    eventsURL = "",
    receivedEventsURL = "",
    type = "",
    siteAdmin = false,
    name = "",
    company = "",
    blog = "",
    location = "",
    email = "",
    hireable = false,
    bio = "",
    twitterUsername = "",
    publicRepos = 0L,
    publicGists = 0L,
    followers = 0L,
    following = 0L,
    createdAt = "",
    updatedAt = "",
)