package com.github.charbgr.reproduciblehistory

import android.util.Log
import com.github.charbgr.reproduciblehistory.github.GitHubApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class MainScreenPresenter {

  private lateinit var viewRef: WeakReference<MainScreenView>
  private lateinit var compositeDisposable: CompositeDisposable

  private val gitHubApi: GitHubApi by lazy { GitHubApi.create() }

  fun init(mainView: MainScreenView) {
    viewRef = WeakReference(mainView)
    compositeDisposable = CompositeDisposable()
  }

  fun bindIntents() {
    val searchIntent = viewRef.get()?.searchIntent()?.share() ?: Observable.empty()
    val historyIntent = viewRef.get()?.historyIntent()?.share() ?: Observable.empty()

    searchIntent
        .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .doOnNext { Log.d("Intent", "Received Search Intent") }
        .switchMap { searchTerm ->
          gitHubApi.githubDAO.searchUser(searchTerm)
              .map { MainScreenViewModel.success(searchTerm, it) }
              .doOnError { it.printStackTrace() }
              .onErrorReturn { MainScreenViewModel.error(searchTerm) }
              .startWith(MainScreenViewModel.inProgress(searchTerm))
        }
        .doOnNext { Log.d("Render", it.toString()) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object : DisposableObserver<MainScreenViewModel>() {
          override fun onNext(viewModel: MainScreenViewModel) {
            dispatchRender(viewModel)
          }

          override fun onError(e: Throwable) {
            Log.wtf("ragment", e.message)
          }

          override fun onComplete() {
          }

        })
        .addTo(compositeDisposable)

    historyIntent
        .subscribeWith(object : DisposableObserver<Int>() {
          override fun onNext(idx: Int) {
            val validIdx = Math.max(0, idx - 1)

            val viewModels = ViewModelCache.getAll()
            if (viewModels.isEmpty()) return

            val viewModel = viewModels[validIdx]
            viewRef.get()?.renderFromHistory(viewModel)
          }

          override fun onComplete() {
          }

          override fun onError(e: Throwable) {
            Log.wtf("ragment", e.message)
          }
        })
        .addTo(compositeDisposable)
  }

  private fun dispatchRender(viewModel: MainScreenViewModel) {
    ViewModelCache.append(viewModel)
    viewRef.get()?.render(viewModel)
  }

  fun destroy() {
    viewRef.clear()
    compositeDisposable.dispose()
  }
}