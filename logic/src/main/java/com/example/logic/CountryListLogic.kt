package com.example.logic

import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.errors.CountryListError
import com.example.repositories.CountryListPushBasedRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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

    fun canAccessCountry(country: Country): Completable = when (country) {
        NoPermissionsCountry -> Completable.error(CountryListError.NotEnoughPermissionsError)
        UnavailableCountry -> Completable.error(CountryListError.NotAvailableError)
        BlockedCountry -> Completable.error(CountryListError.BlockedCountry)
        else -> Completable.complete()
    }

    fun getRandomCountry(): Single<Country> {
        return repository.getRandomCountry()
    }

    companion object {
        private val NoPermissionsCountry = Country(regionCode = "No_Permissions")
        private val UnavailableCountry = Country(regionCode = "Unavailable")
        private val BlockedCountry = Country(regionCode = "Blocked")
        private val XXCountry = Country(regionCode = "xx")

        private val InvalidCountries = listOf(XXCountry, NoPermissionsCountry, UnavailableCountry, BlockedCountry)
        val InvalidContinent = Continent(name = "Invalid", countries = InvalidCountries)
    }
}