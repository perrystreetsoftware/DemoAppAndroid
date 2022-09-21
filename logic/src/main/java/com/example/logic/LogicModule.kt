package com.example.logic
import org.koin.dsl.module

val logicModule = module {
    factory<CountryDetailsLogic> {
        CountryDetailsLogic(repository = get())
    }

    factory<CountrySelectingLogic> {
        CountrySelectingLogic(repository = get())
    }
}