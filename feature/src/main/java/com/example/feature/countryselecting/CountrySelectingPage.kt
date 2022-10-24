package com.example.feature.countryselecting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.domainmodels.ServerStatus
import com.example.feature.countryselecting.componenets.CountrySelectingButton
import com.example.feature.countryselecting.componenets.CountrySelectingList
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountrySelectingViewModel

@Composable
fun CountrySelectingPage(
    state: CountrySelectingViewModel.UiState,
    onCountrySelected: ((Country) -> Unit)? = null,
    onRefreshTapped: (() -> Unit)? = null
) {
    Box {
        ProgressIndicator(isLoading = state.isLoading)
        Column(modifier = Modifier.fillMaxHeight()) {
            Column(modifier = Modifier.weight(1f)) {
                CountrySelectingList(
                    list = state.continents,
                    onClick = onCountrySelected
                )
            }
            if (state.serverStatus?.success == true) {
                Text("Servers are OK")
            } else {
                Text("Servers are NOT OK")
            }
            CountrySelectingButton(
                isLoaded = state.isLoaded,
                onClick = onRefreshTapped
            )
        }
    }
}

@Preview(name = "Standard Preview", widthDp = 300, heightDp = 300)
@Composable
fun CountrySelectingPagePreview() {
    CountrySelectingPage(
        state = CountrySelectingViewModel.UiState(continents = listOf(Continent("North America", countries = listOf(Country("ca")))))
    )
}
