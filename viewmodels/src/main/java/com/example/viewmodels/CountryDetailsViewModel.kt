package com.example.viewmodels

import com.example.domainmodels.CountryDetailsUIModel
import com.example.domainmodels.CountryUIModel
import com.example.logic.CountryDetailsLogic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryDetailsViewModel(private val country: CountryUIModel, private val logic: CountryDetailsLogic) {
    enum class State {
        Initial,
        Loading
    }

    private var _state: BehaviorSubject<CountryDetailsViewModel.State> = BehaviorSubject.createDefault(
        CountryDetailsViewModel.State.Initial)
    val state: Observable<CountryDetailsViewModel.State> = _state

    private var _details: BehaviorSubject<CountryDetailsUIModel> = BehaviorSubject.createDefault(CountryDetailsUIModel.EMPTY)
    val details: Observable<CountryDetailsUIModel> = _details
    private var disposables = CompositeDisposable()

    fun onAppear() {
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