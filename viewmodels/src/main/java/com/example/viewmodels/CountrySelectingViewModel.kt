package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
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

class CountrySelectingViewModel(val logic: CountrySelectingLogic) : ViewModel() {
    sealed class State {
        object Initial: State()
        object Loading: State()
        data class Loaded(val myContinents: List<Continent>): State()

        val isLoading: Boolean
            get() = this is Loading

        val isLoaded: Boolean
            get() = this is Loaded

        val continents: List<Continent>
            get() {
                return when(this) {
                    is Loaded -> {
                        this.myContinents
                    }
                    else -> emptyList()
                }
            }
    }

    private var _state: BehaviorSubject<State> = BehaviorSubject.createDefault(State.Initial)
    val state: Observable<State> = _state
    private val disposables = CompositeDisposable()
    val continents: Observable<List<Continent>> = logic.continents

    sealed class Event {
        data class Error(val error: CountrySelectingViewModelError): Event()
        object ErrorDisappear: Event()
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

    fun onErrorDismissed() {
        _events.onNext(Event.ErrorDisappear)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}