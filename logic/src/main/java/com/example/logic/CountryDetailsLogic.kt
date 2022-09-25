package com.example.logic

import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
import com.example.repositories.CountryDetailsRepository
import io.reactivex.rxjava3.core.Observable
import com.example.networklogic.TravelAdvisoryApiError

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

class CountryDetailsLogic(private val repository: CountryDetailsRepository) {
    fun getDetails(country: Country): Observable<CountryDetails> {
        return repository.getDetails(country.regionCode).map {
            CountryDetails(it)
        }
    }
}