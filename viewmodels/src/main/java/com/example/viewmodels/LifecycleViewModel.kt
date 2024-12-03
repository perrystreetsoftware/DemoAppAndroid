package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.utils.Optional
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

open class LifecycleViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()
    private var didViewAppear = false

    protected val mutableError = BehaviorSubject.createDefault<Optional<Throwable>>(Optional.empty())
    val error: Observable<Optional<Throwable>> = mutableError

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun onViewAppear() {
        if (!didViewAppear) {
            onFirstAppear()
            didViewAppear = true
        }
        onEveryAppear()
    }

    fun clearLastError() {
        mutableError.onNext(Optional.empty())
    }

    protected open fun onFirstAppear() = Unit
    protected open fun onEveryAppear() = Unit
}
