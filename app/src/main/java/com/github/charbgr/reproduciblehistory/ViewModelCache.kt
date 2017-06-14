package com.github.charbgr.reproduciblehistory

object ViewModelCache {

  private val viewModels: MutableList<MainScreenViewModel> = mutableListOf()

  fun getAll(): List<MainScreenViewModel> = viewModels.toList()

  fun getLast(n: Int): List<MainScreenViewModel> {
    return viewModels.sortedBy { it.createdAt }.takeLast(n)
  }

  fun getLastFive(): List<MainScreenViewModel> = getLast(5)

  fun append(viewModel: MainScreenViewModel) {
    viewModels.add(viewModel)
  }
}
