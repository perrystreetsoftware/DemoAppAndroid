package com.example.uicomponents.countryselecting

import com.example.domainmodels.ContinentDomainModel
import com.example.viewmodels.CountrySelectingViewModel

data class CountrySelectingUIState(
    val continents: List<ContinentDomainModel>,
    val state: CountrySelectingViewModel.State
) {

}