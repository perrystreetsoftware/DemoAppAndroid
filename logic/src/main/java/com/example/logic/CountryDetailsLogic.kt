package com.example.logic

import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
import com.example.repositories.CountryDetailsRepository
import io.reactivex.rxjava3.core.Observable

class CountryDetailsLogic(private val repository: CountryDetailsRepository) {
    fun getDetails(country: Country): Observable<CountryDetails> {
        return repository.getDetails(country.regionCode).map {
            CountryDetails(it)
        }
    }
}