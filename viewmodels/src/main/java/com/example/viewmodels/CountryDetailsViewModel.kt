package com.example.viewmodels

import com.example.domainmodels.CountryDetailsDomainModel
import com.example.domainmodels.CountryDomainModel
import com.example.logic.CountryDetailsLogic
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryDetailsViewModel(private val country: CountryDomainModel, private val logic: CountryDetailsLogic) {
    enum class State {
        Initial,
        Loading
    }

    private var _state: BehaviorSubject<CountryDetailsViewModel.State> = BehaviorSubject.createDefault(
        CountryDetailsViewModel.State.Initial)
    val state: Observable<CountryDetailsViewModel.State> = _state

    private var _details: BehaviorSubject<CountryDetailsDomainModel> = BehaviorSubject.createDefault(CountryDetailsDomainModel.EMPTY)
    val details: Observable<CountryDetailsDomainModel> = _details
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