package com.rittmann.users

sealed class UserNavigation(val destination: String) {
    object Users : UserNavigation("users")

    object UserData : UserNavigation(
        "user_data/{user}"
    ) {
        const val KEY = "user"
        fun transformDestination(user: String) = "user_data/$user"
    }
}