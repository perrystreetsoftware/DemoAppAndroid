package com.example.repositories

import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.interfaces.ITravelAdvisoriesApi
import io.reactivex.rxjava3.core.Observable

// This is a pull-based repository
class CountryDetailsPullBasedRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    fun getDetails(regionCode: String): Observable<CountryDetails> {
        return travelAdvisoriesApi.getCountryDetails(regionCode = regionCode).map {
            CountryDetails(Country(regionCode = it.regionCode), it.legalCodeBody)
        }
    }
}