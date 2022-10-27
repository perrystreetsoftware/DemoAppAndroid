package com.example.repositories

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.TravelAdvisoryApiError
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


sealed class CountryListRepositoryError: Throwable() {
    object Forbidden: CountryListRepositoryError()
    object Other: CountryListRepositoryError()

    companion object {
        fun fromThrowable(throwable: Throwable): CountryListRepositoryError {
            return when(throwable) {
                is TravelAdvisoryApiError.Forbidden -> { Forbidden }
                else -> { Other }
            }
        }
    }
}

// This is a push-based repository example
class CountryListPushBasedRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    private var _continents: BehaviorSubject<List<Continent>> = BehaviorSubject.createDefault(emptyList())
    val continents: Observable<List<Continent>> = _continents

    fun reload(): Completable {
        return travelAdvisoriesApi.getCountryList()
            .doOnNext {
                val continents = listOf(
                    Pair("Africa", it.africa),
                    Pair("Asia", it.asia),
                    Pair("Latin America", it.latam),
                    Pair("Oceania", it.oceania),
                    Pair("Europe", it.europe)
                )

                this._continents.onNext(
                    continents.map { pair ->
                        Continent(
                            name = pair.first,
                            countries = pair.second.map { regionCode ->
                                Country(
                                    regionCode = regionCode
                                )
                            }
                        )
                    }
                )
            }
            .onErrorReturn {
                throw CountryListRepositoryError.fromThrowable(it)
            }
            .ignoreElements()
    }

    fun getForbiddenApi(): Completable {
        return travelAdvisoriesApi.getForbiddenApi()
    }
}