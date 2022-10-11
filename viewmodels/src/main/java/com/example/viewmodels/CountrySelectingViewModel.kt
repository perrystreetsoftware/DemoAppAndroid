package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.logic.CountrySelectingLogic
import com.example.logic.CountrySelectingLogicError
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
    sealed class ErrorState {
        object None: ErrorState()
        data class Error(val throwable: CountrySelectingViewModelError): ErrorState()
    }

    sealed class State {
        object Initial : State()
        object Loading : State()
        data class Loaded(val myContinents: List<Continent>) : State()

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
    private val _errorState: BehaviorSubject<ErrorState> = BehaviorSubject.createDefault(ErrorState.None)
    val errorState: Observable<ErrorState> = _errorState

//    sealed class Event {
//        object Appear : Event()
//        data class ErrorAppear(val error: CountrySelectingViewModelError) : Event()
//        object ErrorDisappear : Event()
//        data class CountrySelect(val domainModel: Country) : Event()
//    }

//    private var _events: PublishSubject<Event> = PublishSubject.create()
//    val events: Observable<Event> = _events

    fun onCountrySelected(country: Country) {
//        _events.onNext(Event.CountrySelect(country))
    }

    fun onPageLoaded() {
        _state.value?.let { state ->
            if (!state.isLoaded) {
                disposables.add(
                    logic.reload().doOnSubscribe { _state.onNext(State.Loading) }
                        .andThen(logic.continents)
                        .subscribe({
                            _state.onNext(State.Loaded(it))
                        }, { error ->
                            print("error: $error")
                            _state.onNext(State.Initial)
                        })
                )
            }
        }
    }

    fun onButtonTapped() {
        disposables.add(
            logic.getForbiddenApi().subscribe({
            }, { error ->
                _errorState.onNext(ErrorState.Error(CountrySelectingViewModelError.fromThrowable(error)))
            })
        )
    }

    fun onErrorDismissed() {
        _errorState.onNext(ErrorState.None)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}