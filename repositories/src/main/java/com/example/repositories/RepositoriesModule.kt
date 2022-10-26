package com.example.repositories

import org.koin.dsl.module

val repositoriesModule = module {
    single<CountryDetailsPullBasedRepository> {
        CountryDetailsPullBasedRepository(travelAdvisoriesApi = get())
    }

    single<CountryListPushBasedRepository> {
        CountryListPushBasedRepository(travelAdvisoriesApi = get())
    }

    single<ServerStatusPushBasedRepository> {
        ServerStatusPushBasedRepository(travelAdvisoriesApi = get())
    }
}
