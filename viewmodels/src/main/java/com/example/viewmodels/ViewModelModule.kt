package com.example.viewmodels

import org.koin.dsl.module

val viewModelModule = module {
    factory<CountrySelectingViewModel> {
        CountrySelectingViewModel(logic = get())
    }

    factory<CountryDetailsViewModel> { country ->
        CountryDetailsViewModel(
            country = country.get(),
            logic = get()
        )
    }
}