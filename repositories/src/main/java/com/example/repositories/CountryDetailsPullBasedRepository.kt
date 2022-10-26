package com.example.repositories

import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.TravelAdvisoryApiError
import io.reactivex.rxjava3.core.Observable

sealed class CountryDetailsError: Throwable() {
    object CountryNotFound: CountryDetailsError()
    data class Other(val throwable: Throwable): CountryDetailsError()

    companion object {
        fun fromThrowable(error: Throwable): CountryDetailsError {
            return when(error) {
                is TravelAdvisoryApiError.CountryNotFound -> CountryNotFound
                else -> {
                    return Other(error)
                }
            }
        }
    }
}

// This is a pull-based repository
class CountryDetailsPullBasedRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    fun getDetails(regionCode: String): Observable<CountryDetails> {
        return travelAdvisoriesApi.getCountryDetails(regionCode = regionCode).map {
            CountryDetails(Country(regionCode = it.regionCode), it.legalCodeBody)
        }.onErrorReturn {
            throw CountryDetailsError.fromThrowable(it)
        }
    }
}