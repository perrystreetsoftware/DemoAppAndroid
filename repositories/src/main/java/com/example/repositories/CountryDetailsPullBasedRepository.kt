package com.example.repositories

import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.errors.CountryDetailsError
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.TravelAdvisoryApiError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun Throwable.toCountryDetailsError(): CountryDetailsError {
    return when(this) {
        is TravelAdvisoryApiError.CountryNotFound -> CountryDetailsError.CountryNotFound
        else -> {
            return CountryDetailsError.Other(this)
        }
    }
}

// This is a pull-based repository
class CountryDetailsPullBasedRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    fun getDetails(regionCode: String): Single<CountryDetails> {
        return travelAdvisoriesApi.getCountryDetails(regionCode = regionCode).map {
            CountryDetails(Country(regionCode = it.regionCode), it.legalCodeBody)
        }.onErrorResumeNext {
            Single.error(it.toCountryDetailsError())
        }
    }
}