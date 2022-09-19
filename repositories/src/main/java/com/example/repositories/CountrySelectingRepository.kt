package com.example.repositories

import com.example.domainmodels.ContinentUIModel
import com.example.domainmodels.CountryUIModel
import com.example.interfaces.TravelApiImplementing
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

class CountrySelectingRepository(private val travelAdvisoriesApi: TravelApiImplementing) {
    private var _continents: BehaviorSubject<List<ContinentUIModel>> = BehaviorSubject.createDefault(emptyList())
    var continents: Observable<List<ContinentUIModel>> = _continents

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
                        ContinentUIModel(
                            name = pair.first,
                            countries = pair.second.map { regionCode ->
                                CountryUIModel(
                                    countryName = Locale("", regionCode).displayCountry,
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