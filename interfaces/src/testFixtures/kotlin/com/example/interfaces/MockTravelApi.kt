package com.example.interfaces

import com.example.dtos.CountryDetailsDTO
import com.example.dtos.CountryListDTO
import com.example.dtos.ServerStatusDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MockTravelApi: ITravelAdvisoriesApi {
    override fun getForbiddenApi(): Completable {
        return Completable.complete()
    }

    override fun getServerStatus(): Observable<ServerStatusDTO> {
        return Observable.just(ServerStatusDTO.EMPTY)
    }

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