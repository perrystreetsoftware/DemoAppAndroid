package com.example.viewmodels

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AboutViewModel)
    viewModelOf(::CountryListViewModel)

    viewModel { params ->
        CountryDetailsViewModel(
            logic = get(),
            regionCode = params.get()
        )
    }
}