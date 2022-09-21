package com.example.repositories

import com.example.domainmodels.CountryDetailsDTO
import com.example.interfaces.ITravelAdvisoriesApi
import io.reactivex.rxjava3.core.Observable

class CountryDetailsRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    fun getDetails(regionCode: String): Observable<CountryDetailsDTO> {
        return travelAdvisoriesApi.getCountryDetails(regionCode = regionCode)
    }
}