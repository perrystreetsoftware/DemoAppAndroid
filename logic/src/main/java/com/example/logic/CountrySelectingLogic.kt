package com.example.logic

import com.example.domainmodels.ContinentUIModel
import com.example.repositories.CountrySelectingRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class CountrySelectingLogic(private val repository: CountrySelectingRepository) {
    val continents: Observable<List<ContinentUIModel>> = repository.continents

    fun reload(): Completable {
        return repository.reload()
    }
}