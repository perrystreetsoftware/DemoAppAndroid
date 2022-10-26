package com.example.logic

import com.example.domainmodels.CountryDetails
import com.example.repositories.CountryDetailsPullBasedRepository
import io.reactivex.rxjava3.core.Observable

class CountryDetailsLogic(private val repository: CountryDetailsPullBasedRepository) {
    fun getDetails(regionCode: String): Observable<CountryDetails> {
        return repository.getDetails(regionCode)
    }
}