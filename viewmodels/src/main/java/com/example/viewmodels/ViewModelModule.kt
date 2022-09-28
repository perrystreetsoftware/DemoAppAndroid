package com.example.viewmodels

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CountrySelectingViewModel(logic = get())
    }

    viewModel {
        CountryDetailsViewModel(
            logic = get()
        )
    }
}