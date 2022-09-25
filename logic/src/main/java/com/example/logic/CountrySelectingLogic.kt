package com.example.logic

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.networklogic.TravelAdvisoryApiError
import com.example.repositories.CountrySelectingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

sealed class CountrySelectingLogicError: Throwable() {
    object Forbidden: CountrySelectingLogicError()
    object Other: CountrySelectingLogicError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountrySelectingLogicError {
            return when(throwable) {
                is TravelAdvisoryApiError.Forbidden -> { Forbidden }
                else -> { Other }
            }
        }
    }
}

class CountrySelectingLogic(private val repository: CountrySelectingRepository) {
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