package com.github.charbgr.reproduciblehistory

import com.github.charbgr.reproduciblehistory.github.dao.GitHubUser

data class MainScreenViewModel(
    val searchTerm: String,
    val showLoading: Boolean,
    val showFields: Boolean,
    val showError: Boolean,
    val gitHubUser: GitHubUser? = null,
    val errorMessage: String? = null
) {

  val createdAt: Long = System.currentTimeMillis()

  companion object {
    fun inProgress(searchTerm: String): MainScreenViewModel = MainScreenViewModel(searchTerm, true, false, false)
    fun success(searchTerm: String, gitHubUser: GitHubUser) = MainScreenViewModel(searchTerm, false, true, false, gitHubUser)
    fun error(searchTerm: String): MainScreenViewModel = MainScreenViewModel(searchTerm, false, false, true, null,
        "Houston we have a problem")
  }
}
