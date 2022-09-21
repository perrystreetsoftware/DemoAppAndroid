package com.example.uicomponents.countryselecting

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.Country
import com.example.uicomponents.countryselecting.componenets.CountrySelectingList
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountrySelectingViewModel

@Composable
fun CountrySelectingPage(
    state: CountrySelectingUIState,
    onClick: ((Country) -> Unit)? = null
) {
    ProgressIndicator(isLoading = state.viewModelState.isLoading)
    CountrySelectingList(
        list = state.continents,
        onClick = onClick
    )
}

@Preview
@Composable
fun CountrySelectingPagePreview() {
    CountrySelectingPage(
        CountrySelectingUIState(
            continents = emptyList(),
            viewModelState = CountrySelectingViewModel.State.Initial
        )
    )
}
