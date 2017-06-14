package com.github.charbgr.reproduciblehistory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.SeekBarStopChangeEvent
import com.jakewharton.rxbinding2.widget.changeEvents
import com.jakewharton.rxbinding2.widget.textChangeEvents
import io.reactivex.Observable

class MainActivity : AppCompatActivity(), MainScreenView {


  private val presenter: MainScreenPresenter by lazy {
    MainScreenPresenter()
  }

  private lateinit var searchEditText: EditText
  private lateinit var userName: TextView
  private lateinit var userLogin: TextView
  private lateinit var gitHubUrl: TextView
  private lateinit var progressbar: ProgressBar
  private lateinit var historyBar: SeekBar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setUpViews()

    presenter.init(this)
    presenter.bindIntents()
  }

  override fun onDestroy() {
    presenter.destroy()
    super.onDestroy()
  }

  private fun setUpViews() {
    searchEditText = findViewById(R.id.search_user_name) as EditText
    userName = findViewById(R.id.user_name) as TextView
    userLogin = findViewById(R.id.user_login) as TextView
    gitHubUrl = findViewById(R.id.user_github_url) as TextView
    progressbar = findViewById(R.id.progress) as ProgressBar
    historyBar = findViewById(R.id.seekBar) as SeekBar
  }

  override fun searchIntent(): Observable<String> = searchEditText.textChangeEvents()
                                                                  .filter { it.view().isFocused }
                                                                  .map { it.text().toString() }

  override fun historyIntent(): Observable<Int> {
    return historyBar.changeEvents()
                     .filter { it is SeekBarStopChangeEvent }
                     .map { it.view().progress }
  }

  private fun render(viewModel: MainScreenViewModel, isRenderingHistory: Boolean) {
    if (!isRenderingHistory) {
      historyBar.increaseMaxAndProgress()
    } else {
      historyBar.requestFocus()
      searchEditText.setText(viewModel.searchTerm, TextView.BufferType.EDITABLE)
    }

    progressbar.showOrHideInvisible(viewModel.showLoading)
    userName.showOrHideInvisible(viewModel.showFields)
    userLogin.showOrHideInvisible(viewModel.showFields)
    gitHubUrl.showOrHideInvisible(viewModel.showFields)

    userName.text = viewModel.gitHubUser?.name
    userLogin.text = viewModel.gitHubUser?.login
    gitHubUrl.text = viewModel.gitHubUser?.url

    if (viewModel.showError) {
      Toast.makeText(this, viewModel.errorMessage, Toast.LENGTH_LONG).show()
    }
  }

  override fun render(viewModel: MainScreenViewModel) {
    render(viewModel, false)
  }

  override fun renderFromHistory(viewModel: MainScreenViewModel) {
    render(viewModel, true)
  }
}
