package com.example.repositories

import org.koin.dsl.module

val repositoriesModule = module {
    single<CountryDetailsRepository> {
        CountryDetailsRepository(travelAdvisoriesApi = get())
    }

    single<CountrySelectingRepository> {
        CountrySelectingRepository(travelAdvisoriesApi = get())
    }
}
