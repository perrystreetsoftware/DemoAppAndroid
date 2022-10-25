package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.CountryDetails
import com.example.logic.CountryDetailsLogic
import com.example.logic.CountryDetailsLogicError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

sealed class CountryDetailsViewModelError: Throwable() {
    object CountryNotFound: CountryDetailsViewModelError()
    object Unknown: CountryDetailsViewModelError()

    companion object {
        fun fromLogicError(error: Throwable): CountryDetailsViewModelError {
            when (error) {
                is CountryDetailsLogicError.CountryNotFound -> { CountryNotFound }
                else -> { Unknown }
            }
            return Unknown
        }
    }
}

class CountryDetailsViewModel(private val logic: CountryDetailsLogic, regionCode: String): ViewModel() {
    sealed class State() {
        object Initial: State()
        object Loading: State()
        data class Loaded(val details: CountryDetails): State()
        data class Error(val error: CountryDetailsViewModelError): State()
    }

    private var _state: BehaviorSubject<State> = BehaviorSubject.createDefault(
        State.Initial)
    val state: Observable<State> = _state

    private var disposables = CompositeDisposable()

    init {
        onPageLoaded(regionCode)
    }

    private fun onPageLoaded(regionCode: String) {
        disposables.add(
            logic.getDetails(regionCode = regionCode)
                .doOnSubscribe {
                    _state.onNext(State.Loading)
                }
                .subscribe({ it ->
                    _state.onNext(State.Loaded(it))
                }, { error ->
                    _state.onNext(State.Error(CountryDetailsViewModelError.fromLogicError(error)))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}