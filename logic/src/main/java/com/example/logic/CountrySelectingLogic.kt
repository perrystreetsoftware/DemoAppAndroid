package com.example.logic

import com.example.domainmodels.Continent
import com.example.repositories.CountrySelectingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class CountrySelectingLogic(private val repository: CountrySelectingRepository) {
    val continents: Observable<List<Continent>> = repository.continents

    fun reload(): Completable {
        return repository.reload()
    }
}