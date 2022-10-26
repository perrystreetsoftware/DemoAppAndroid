package com.example.logic

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.networklogic.TravelAdvisoryApiError
import com.example.repositories.CountryListPushBasedRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

sealed class CountryListLogicError: Throwable() {
    object Forbidden: CountryListLogicError()
    object Other: CountryListLogicError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountryListLogicError {
            return when(throwable) {
                is TravelAdvisoryApiError.Forbidden -> { Forbidden }
                else -> { Other }
            }
        }
    }
}

class CountryListLogic(private val repository: CountryListPushBasedRepository) {
    val continents: Observable<List<Continent>>
        get() {
            return repository.continents.map {
                if (it.isNotEmpty()) {
                    listOf(InvalidContinent) + it
                } else {
                    it
                }
            }
        }

    fun reload(): Completable {
        return repository.reload()
    }

    fun getForbiddenApi(): Completable {
        return repository.getForbiddenApi()
    }

    companion object {
        val InvalidContinent = Continent(name = "Invalid", countries = listOf(Country(regionCode = "xx")))
    }
}