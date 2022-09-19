package com.example.logic

import com.example.domainmodels.CountryDetailsUIModel
import com.example.domainmodels.CountryUIModel
import com.example.repositories.CountryDetailsRepository
import io.reactivex.rxjava3.core.Observable

class CountryDetailsLogic(private val repository: CountryDetailsRepository) {
    fun getDetails(country: CountryUIModel): Observable<CountryDetailsUIModel> {
        return repository.getDetails(country.regionCode).map {
            CountryDetailsUIModel(it)
        }
    }
}