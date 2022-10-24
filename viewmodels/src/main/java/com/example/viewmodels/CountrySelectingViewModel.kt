package com.example.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.domainmodels.Continent
import com.example.domainmodels.ServerStatus
import com.example.logic.CountrySelectingLogic
import com.example.logic.CountrySelectingLogicError
import com.example.logic.ServerStatusLogic
import com.example.networklogic.TravelAdvisoryApiError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject


sealed class CountrySelectingViewModelError(): Throwable() {
    object Forbidden: CountrySelectingViewModelError()
    object Unknown: CountrySelectingViewModelError()
    object ConnectionError: CountrySelectingViewModelError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountrySelectingViewModelError {
            return when(throwable) {
                is CountrySelectingLogicError.Forbidden -> { Forbidden }
                is TravelAdvisoryApiError -> { ConnectionError }
                else -> Unknown
            }
        }
    }
}

class CountrySelectingViewModel(val logic: CountrySelectingLogic, val serverStatusLogic: ServerStatusLogic) : ViewModel() {
    data class UiState(val continents: List<Continent> = emptyList(),
                       val isLoading: Boolean = false,
                       val isLoaded: Boolean = false,
                       val error: CountrySelectingViewModelError? = null,
                       val serverStatus: ServerStatus? = null
    ) {
    }

    private val _state: BehaviorSubject<UiState> = BehaviorSubject.createDefault(UiState())
    val state: Observable<UiState> = _state
    private val disposables = CompositeDisposable()

    @VisibleForTesting
    fun onPageLoaded() {
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
                        error = CountrySelectingViewModelError.fromThrowable(error))
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
                _state.onNext(_state.value!!.copy(error = CountrySelectingViewModelError.fromThrowable(error)))
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