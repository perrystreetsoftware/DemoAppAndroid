package com.example.logic

import com.example.domainmodels.CountryDetails
import com.example.repositories.CountryDetailsPullBasedRepository
import io.reactivex.rxjava3.core.Observable
import com.example.interfaces.TravelAdvisoryApiError

sealed class CountryDetailsLogicError: Throwable() {
    object CountryNotFound: CountryDetailsLogicError()
    data class Other(val travelAdvisoryApiError: TravelAdvisoryApiError): CountryDetailsLogicError()

    companion object {
        fun fromTravelAdvisoriyApiError(error: TravelAdvisoryApiError): CountryDetailsLogicError {
            return when(error) {
                is TravelAdvisoryApiError.CountryNotFound -> CountryNotFound
                else -> {
                    return Other(error)
                }
            }
        }
    }
}

class CountryDetailsLogic(private val repository: CountryDetailsPullBasedRepository) {
    fun getDetails(regionCode: String): Observable<CountryDetails> {
        return repository.getDetails(regionCode).map {
            CountryDetails(it)
        }
    }
}