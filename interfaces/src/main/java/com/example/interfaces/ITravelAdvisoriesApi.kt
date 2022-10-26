package com.example.interfaces
import com.example.dtos.CountryDetailsDTO
import com.example.dtos.CountryListDTO
import com.example.dtos.ServerStatusDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface ITravelAdvisoriesApi {
    fun getForbiddenApi(): Completable
    fun getCountryList(): Observable<CountryListDTO>
    fun getCountryDetails(regionCode: String): Observable<CountryDetailsDTO>
    fun getServerStatus(): Observable<ServerStatusDTO>
}