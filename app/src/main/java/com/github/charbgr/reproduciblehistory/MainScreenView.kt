package com.github.charbgr.reproduciblehistory

import io.reactivex.Observable

interface MainScreenView {

  fun searchIntent(): Observable<String>
  fun historyIntent(): Observable<Int>

  fun render(viewModel: MainScreenViewModel)
  fun renderFromHistory(viewModel: MainScreenViewModel)
}
