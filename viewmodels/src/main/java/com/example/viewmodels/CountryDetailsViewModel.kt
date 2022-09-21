package com.example.viewmodels

import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
import com.example.logic.CountryDetailsLogic
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryDetailsViewModel(private val country: Country, private val logic: CountryDetailsLogic) {
    enum class State {
        Initial,
        Loading;

        val isLoading: Boolean
            get() = this == Loading
    }

    private var _state: BehaviorSubject<CountryDetailsViewModel.State> = BehaviorSubject.createDefault(
        CountryDetailsViewModel.State.Initial)
    val state: Observable<CountryDetailsViewModel.State> = _state

    private var _details: BehaviorSubject<CountryDetails> = BehaviorSubject.createDefault(CountryDetails.EMPTY)
    val details: Observable<CountryDetails> = _details
    private var disposables = CompositeDisposable()

    fun onPageLoaded() {
        disposables.add(
            logic.getDetails(country = country)
                .doOnSubscribe {
                    _state.onNext(State.Loading)
                }
                .doOnNext {
                    _details.onNext(it)
                }
                .doOnComplete {
                    _state.onNext(State.Initial)
                }
                .doOnError {
                    _state.onNext(State.Initial)
                }
                .subscribe()
        )
    }
}