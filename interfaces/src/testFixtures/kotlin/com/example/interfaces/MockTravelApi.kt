package com.example.interfaces

import com.example.domainmodels.CountryDetailsDTO
import com.example.domainmodels.CountryListDTO
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MockTravelApi: TravelApiImplementing {
    var getCountryListResult: Observable<CountryListDTO>? = null
    override fun getCountryList(): Observable<CountryListDTO> {
        getCountryListResult?.let {
            return it
        } ?: run {
            return Observable.just(CountryListDTO(africa = listOf("ug", "ng")))
                .observeOn(Schedulers.computation())
        }
    }

    var getCountryDetailsResult: Observable<CountryDetailsDTO>? = null
    override fun getCountryDetails(regionCode: String): Observable<CountryDetailsDTO> {
        getCountryDetailsResult?.let {
            return it
        } ?: run {
            return Observable.just(CountryDetailsDTO(area = "Asia", regionName = "Yemen", regionCode = "YE", legalCodeBody = "Article 264"))
                .observeOn(Schedulers.computation())
        }
    }
}