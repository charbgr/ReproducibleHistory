package com.github.charbgr.reproduciblehistory.github.dao

import com.squareup.moshi.Json

data class GitHubUser(

    @Json(name = "login")
    var login: String? = null,

    @Json(name = "id")
    var id: String? = null,

    @Json(name = "avatar_url")
    var avatar_url: String? = null,

    @Json(name = "url")
    var url: String? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "email")
    var email: String? = null,

    @Json(name = "location")
    var location: String? = null
)