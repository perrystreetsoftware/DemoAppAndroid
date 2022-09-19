package com.example.uicomponents.countryselecting

import com.example.domainmodels.ContinentUIModel
import com.example.viewmodels.CountrySelectingViewModel

data class CountrySelectingUIState(
    val continents: List<ContinentUIModel>,
    val state: CountrySelectingViewModel.State
) {

}