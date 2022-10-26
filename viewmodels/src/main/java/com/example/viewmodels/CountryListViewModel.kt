package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.Continent
import com.example.domainmodels.ServerStatus
import com.example.logic.CountryListLogic
import com.example.logic.ServerStatusLogic
import com.example.interfaces.TravelAdvisoryApiError
import com.example.repositories.CountryListError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

sealed class CountryListViewModelError(): Throwable() {
    object Forbidden: CountryListViewModelError()
    object Unknown: CountryListViewModelError()
    object ConnectionError: CountryListViewModelError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountryListViewModelError {
            return when(throwable) {
                is CountryListError.Forbidden -> { Forbidden }
                is TravelAdvisoryApiError -> { ConnectionError }
                else -> Unknown
            }
        }
    }
}

class CountryListViewModel(val logic: CountryListLogic, val serverStatusLogic: ServerStatusLogic) : ViewModel() {
    data class UiState(val continents: List<Continent> = emptyList(),
                       val isLoading: Boolean = false,
                       val isLoaded: Boolean = false,
                       val error: CountryListViewModelError? = null,
                       val serverStatus: ServerStatus? = null
    ) {
    }

    private val _state: BehaviorSubject<UiState> = BehaviorSubject.createDefault(UiState())
    val state: Observable<UiState> = _state
    private val disposables = CompositeDisposable()

    init {
        // https://stackoverflow.com/questions/73305899/why-launchedeffect-call-second-time-when-i-navigate-back
        onPageLoaded()
    }

    private fun onPageLoaded() {
        disposables.add(logic.continents.doOnNext {
            _state.onNext(_state.value!!.copy(continents = it))
        }.subscribe())

        disposables.add(serverStatusLogic.status.doOnNext {
            _state.onNext(_state.value!!.copy(serverStatus = it))
        }.subscribe())

        disposables.add(
            logic.reload()
                .doOnSubscribe {
                    _state.onNext(_state.value!!.copy(isLoading = true))
                }
                .subscribe({
                    _state.onNext(_state.value!!.copy(isLoading = false, isLoaded = true))
                }, { error ->
                    _state.onNext(_state.value!!.copy(
                        isLoading = false,
                        isLoaded = true,
                        error = CountryListViewModelError.fromThrowable(error))
                    )
                })
        )

        disposables.add(
            serverStatusLogic.reload().subscribe()
        )
    }

    fun onRefreshTapped() {
        disposables.add(
            logic.getForbiddenApi().subscribe({
            }, { error ->
                _state.onNext(_state.value!!.copy(error = CountryListViewModelError.fromThrowable(error)))
            })
        )
    }

    fun onErrorDismissed() {
        _state.onNext(_state.value!!.copy(error = null))
    }

    override fun onCleared() {
        super.onCleared()

        disposables.dispose()
    }
}