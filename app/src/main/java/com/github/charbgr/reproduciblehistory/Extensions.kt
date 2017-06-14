package com.github.charbgr.reproduciblehistory

import android.view.View
import android.widget.SeekBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun View.showOrHideInvisible(isTrue: Boolean) {
    if (isTrue) {
        visibility = View.VISIBLE
    } else {
        visibility = View.INVISIBLE
    }
}

fun SeekBar.increaseMaxAndProgress() {
    max += 1
    progress = max
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}