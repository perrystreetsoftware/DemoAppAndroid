package com.example.viewmodels

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.logic.CountryDetailsLogicError
import com.example.logic.CountrySelectingLogic
import com.example.logic.CountrySelectingLogicError
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

sealed class CountrySelectingViewModelError(): Throwable() {
    object Forbidden: CountrySelectingViewModelError()
    object Unknown: CountrySelectingViewModelError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountrySelectingViewModelError {
            return when(throwable) {
                is CountrySelectingLogicError.Forbidden -> { Forbidden }
                else -> Unknown
            }
        }
    }
}

class CountrySelectingViewModel(val logic: CountrySelectingLogic) {
    enum class State {
        Initial,
        Loading;

        val isLoading: Boolean
            get() = this == Loading
    }

    private var _state: BehaviorSubject<State> = BehaviorSubject.createDefault(State.Initial)
    val state: Observable<State> = _state
    private val disposables = CompositeDisposable()
    val continents: Observable<List<Continent>> = logic.continents

    sealed class Event {
        data class Error(val error: CountrySelectingViewModelError): Event()
        data class Navigate(val domainModel: Country): Event()
    }

    private var _events: PublishSubject<Event> = PublishSubject.create()
    val events: Observable<Event> = _events

    fun onCountrySelected(country: Country) {
        _events.onNext(Event.Navigate(country))
    }

    fun onPageLoaded() {
        disposables.add(
            continents
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

    fun onButtonTapped() {
        disposables.add(
            logic.getForbiddenApi().subscribe({
            }, { error ->
                _events.onNext(Event.Error(CountrySelectingViewModelError.fromThrowable(error)))
            })
        )
    }
}