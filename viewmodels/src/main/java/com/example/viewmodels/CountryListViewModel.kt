package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.domainmodels.ServerStatus
import com.example.errors.CountryListError
import com.example.logic.CountryListLogic
import com.example.logic.ServerStatusLogic
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryListViewModel(val logic: CountryListLogic, val serverStatusLogic: ServerStatusLogic) : ViewModel() {
    data class UiState(
        val continents: List<Continent> = emptyList(),
        val isLoading: Boolean = false,
        val isLoaded: Boolean = false,
        val error: CountryListError? = null,
        val serverStatus: ServerStatus? = null,
        val navigationTarget: Country? = null,
    )

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
                        error = (error as CountryListError)
                    ))
                })
        )

        disposables.add(
            serverStatusLogic.reload().subscribe()
        )
    }

    fun onRefreshTapped() {
        disposables.add(
            logic.getForbiddenApi().subscribe({}, { emitError(it) })
        )
    }

    fun onFailOtherTapped() {
        emitError(CountryListError.Other)
    }

    fun dismissError() {
        _state.onNext(_state.value!!.copy(error = null))
    }

    fun onCountrySelected(country: Country) {
        disposables.add(
            logic.canAccessCountry(country).subscribe(
                {
                    _state.onNext(_state.value!!.copy(navigationTarget = country))
                },
                {
                    emitError(it)
                }
            )
        )
    }

    fun resetNavigationTarget() {
        _state.onNext(_state.value!!.copy(navigationTarget = null))
    }

    fun navigateToRandomCountry() {
        disposables.add(
            logic.getRandomCountry().subscribe(
                { onCountrySelected(it) },
                { emitError(it) }
            )
        )
    }

    private fun emitError(error: Throwable) {
        when (val countryListError = error as CountryListError) {
            is CountryListError.NotEnoughPermissionsError -> {
                _state.onNext(_state.value!!.copy(error = countryListError))
            }
            else -> {
                _state.onNext(_state.value!!.copy(error = countryListError))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        disposables.dispose()
    }
}