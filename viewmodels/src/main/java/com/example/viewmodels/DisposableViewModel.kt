package com.example.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class DisposableViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}