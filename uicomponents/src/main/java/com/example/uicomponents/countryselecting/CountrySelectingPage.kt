package com.example.uicomponents.countryselecting

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.viewmodels.CountrySelectingViewModel

@Composable
fun CountrySelectingPage(
    state: CountrySelectingUIState
) {
    // TODO: Show a progress indicator based on state.state
    CountrySelectingList(list = state.continents)
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
