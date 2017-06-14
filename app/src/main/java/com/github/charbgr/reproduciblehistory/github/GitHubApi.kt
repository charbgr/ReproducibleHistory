package com.github.charbgr.reproduciblehistory.github

import com.github.charbgr.reproduciblehistory.github.dao.GitHubDAO
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubApi private constructor(scheduler: Scheduler) {

  companion object {
    fun create(scheduler: Scheduler = Schedulers.io()): GitHubApi {
      return GitHubApi(scheduler)
    }
  }

  val retrofit: Retrofit by lazy {

    val okHttpClient: OkHttpClient = OkHttpClient
        .Builder()
        .build()

    Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
  }

  val githubDAO: GitHubDAO by lazy {
    retrofit.create(GitHubDAO::class.java)
  }

}