package com.example.uicomponents.countryselecting

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.CountryDomainModel
import com.example.viewmodels.CountrySelectingViewModel

@Composable
fun CountrySelectingPage(
    state: CountrySelectingUIState,
    onClick: ((CountryDomainModel) -> Unit)? = null
) {
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
            state = CountrySelectingViewModel.State.Initial
        )
    )
}
