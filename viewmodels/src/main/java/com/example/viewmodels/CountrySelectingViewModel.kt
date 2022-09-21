package com.example.viewmodels

import com.example.domainmodels.ContinentUIModel
import com.example.logic.CountrySelectingLogic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountrySelectingViewModel(val logic: CountrySelectingLogic) {
    enum class State {
        Initial,
        Loading
    }

    private var _state: BehaviorSubject<State> = BehaviorSubject.createDefault(State.Initial)
    val state: Observable<State> = _state
    private val disposables = CompositeDisposable()
    val continents: Observable<List<ContinentUIModel>> = logic.continents

    fun onPageLoaded() {
        disposables.add(
            continents.take(1)
                .flatMapCompletable {
                    if (it.isEmpty()) {
                        logic.reload().doOnSubscribe {
                                _state.onNext(State.Loading)
                            }
                            .doOnComplete {
                                _state.onNext(State.Initial)
                            }
                            .doOnError {
                                _state.onNext(State.Initial)
                            }
                    } else {
                        Completable.complete()
                    }
                }.subscribe({

                }, { error ->
                    print("error: $error")
                })
        )
    }
}