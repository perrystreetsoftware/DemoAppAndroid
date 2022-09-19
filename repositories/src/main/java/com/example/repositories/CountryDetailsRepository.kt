package com.example.repositories

import com.example.domainmodels.CountryDetailsDTO
import com.example.interfaces.TravelApiImplementing
import io.reactivex.rxjava3.core.Observable

class CountryDetailsRepository(private val travelAdvisoriesApi: TravelApiImplementing) {
    fun getDetails(regionCode: String): Observable<CountryDetailsDTO> {
        return travelAdvisoriesApi.getCountryDetails(regionCode = regionCode)
    }
}