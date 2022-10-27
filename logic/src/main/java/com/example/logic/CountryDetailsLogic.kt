package com.example.logic

import com.example.domainmodels.CountryDetails
import com.example.repositories.CountryDetailsPullBasedRepository
import io.reactivex.rxjava3.core.Single

class CountryDetailsLogic(private val repository: CountryDetailsPullBasedRepository) {
    fun getDetails(regionCode: String): Single<CountryDetails> {
        return repository.getDetails(regionCode)
    }
}