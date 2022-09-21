package com.example.repositories

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.interfaces.ITravelAdvisoriesApi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

class CountrySelectingRepository(private val travelAdvisoriesApi: ITravelAdvisoriesApi) {
    private var _continents: BehaviorSubject<List<Continent>> = BehaviorSubject.createDefault(emptyList())
    var continents: Observable<List<Continent>> = _continents

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
            .ignoreElements()
    }

}