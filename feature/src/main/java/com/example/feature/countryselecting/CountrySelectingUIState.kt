package com.example.feature.countryselecting

import com.example.domainmodels.Continent
import com.example.viewmodels.CountrySelectingViewModel

data class CountrySelectingUIState(
    val continents: List<Continent>,
    val viewModelState: CountrySelectingViewModel.UiState
)