package com.example.interfaces
import com.example.domainmodels.CountryDetailsDTO
import com.example.domainmodels.CountryListDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface ITravelAdvisoriesApi {
    fun getForbiddenApi(): Completable
    fun getCountryList(): Observable<CountryListDTO>
    fun getCountryDetails(regionCode: String): Observable<CountryDetailsDTO>
}