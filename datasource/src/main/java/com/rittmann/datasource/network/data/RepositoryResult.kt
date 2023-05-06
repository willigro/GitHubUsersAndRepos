package com.rittmann.datasource.network.data

import com.google.gson.annotations.SerializedName


class RepositoryResult(
    val id: String = "",

    val name: String? = "",

    @SerializedName("full_name")
    val fullName: String? = "",

    val private: Boolean? = false,

    val description: String? = "",

    @SerializedName("created_at")
    val createdAt: String? = "",

    @SerializedName("stargazers_count")
    val starts: Int? = 0,

    val owner: RepositoryOwner? = null,
)

class RepositoryOwner(
    @SerializedName("id_owner")
    val id: Int? = 0,
    @SerializedName("avatar_url")
    val avatarUrl: String? = "",
)