package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.CountryDetails
import com.example.logic.CountryDetailsLogic
import com.example.repositories.CountryDetailsError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

sealed class CountryDetailsViewModelError: Throwable() {
    object CountryNotFound: CountryDetailsViewModelError()
    object Unknown: CountryDetailsViewModelError()

    companion object {
        fun fromLogicError(error: Throwable): CountryDetailsViewModelError {
            when (error) {
                is CountryDetailsError.CountryNotFound -> { CountryNotFound }
                else -> { Unknown }
            }
            return Unknown
        }
    }
}

class CountryDetailsViewModel(private val logic: CountryDetailsLogic, regionCode: String): ViewModel() {
    sealed class UiState() {
        object Initial: UiState()
        object Loading: UiState()
        data class Loaded(val details: CountryDetails): UiState()
        data class Error(val error: CountryDetailsViewModelError): UiState()
    }

    private var _state: BehaviorSubject<UiState> = BehaviorSubject.createDefault(
        UiState.Initial)
    val state: Observable<UiState> = _state

    private var disposables = CompositeDisposable()

    init {
        onPageLoaded(regionCode)
    }

    private fun onPageLoaded(regionCode: String) {
        disposables.add(
            logic.getDetails(regionCode = regionCode)
                .doOnSubscribe {
                    _state.onNext(UiState.Loading)
                }
                .subscribe({ it ->
                    _state.onNext(UiState.Loaded(it))
                }, { error ->
                    _state.onNext(UiState.Error(CountryDetailsViewModelError.fromLogicError(error)))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}