package com.example.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domainmodels.CountryDetails
import com.example.errors.CountryDetailsError
import com.example.logic.CountryDetailsLogic
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class CountryDetailsViewModel(private val logic: CountryDetailsLogic, regionCode: String) : ViewModel() {
    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Loaded(val details: CountryDetails) : UiState()
        data class Error(val error: CountryDetailsError) : UiState()
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
                    _state.onNext(UiState.Error(error as CountryDetailsError))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}