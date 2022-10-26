package com.example.logic
import org.koin.dsl.module

val logicModule = module {
    factory<CountryDetailsLogic> {
        CountryDetailsLogic(repository = get())
    }

    factory<CountryListLogic> {
        CountryListLogic(repository = get())
    }

    factory<ServerStatusLogic> {
        ServerStatusLogic(repository = get())
    }
}