package com.example.viewmodels

import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
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

class CountryDetailsViewModel(private val country: Country, private val logic: CountryDetailsLogic) {
    sealed class State() {
        object Initial: State()
        object Loading: State()
        data class Error(val error: CountryDetailsViewModelError): State()

        val isLoading: Boolean
            get() {
                return this is Loading
            }
    }

    private var _state: BehaviorSubject<State> = BehaviorSubject.createDefault(
        State.Initial)
    val state: Observable<State> = _state

    private var _details: BehaviorSubject<CountryDetails> = BehaviorSubject.createDefault(CountryDetails.EMPTY)
    val details: Observable<CountryDetails> = _details
    private var disposables = CompositeDisposable()

    fun onPageLoaded() {
        disposables.add(
            logic.getDetails(country = country)
                .doOnSubscribe {
                    _state.onNext(State.Loading)
                }
                .doOnComplete {
                    _state.onNext(State.Initial)
                }
                .subscribe({ it ->
                    _details.onNext(it)
                }, { error ->
                    _state.onNext(State.Error(CountryDetailsViewModelError.fromLogicError(error)))
                })
        )
    }
}