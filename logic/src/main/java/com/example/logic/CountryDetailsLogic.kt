package com.example.logic

import com.example.domainmodels.CountryDetailsDomainModel
import com.example.domainmodels.CountryDomainModel
import com.example.repositories.CountryDetailsRepository
import io.reactivex.rxjava3.core.Observable

class CountryDetailsLogic(private val repository: CountryDetailsRepository) {
    fun getDetails(country: CountryDomainModel): Observable<CountryDetailsDomainModel> {
        return repository.getDetails(country.regionCode).map {
            CountryDetailsDomainModel(it)
        }
    }
}