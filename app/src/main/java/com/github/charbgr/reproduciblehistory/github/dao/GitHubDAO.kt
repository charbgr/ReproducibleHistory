package com.github.charbgr.reproduciblehistory.github.dao

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubDAO {
  @GET("/users/{username}")
  fun searchUser(@Path("username") username: String): Observable<GitHubUser>
}