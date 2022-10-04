package com.example.uicomponents.countryselecting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.uicomponents.countryselecting.componenets.CountrySelectingButton
import com.example.uicomponents.countryselecting.componenets.CountrySelectingList
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountrySelectingViewModel
import com.example.viewmodels.CountrySelectingViewModelError

@Composable
fun CountrySelectingPage(
    state: CountrySelectingViewModel.State,
    onClick: ((Country) -> Unit)? = null,
    onButtonClick: (() -> Unit)? = null
) {
    Box {
        ProgressIndicator(isLoading = state.isLoading)
        Column(modifier = Modifier.fillMaxHeight()) {
            Column(modifier = Modifier.weight(1f)) {
                CountrySelectingList(
                    list = state.continents,
                    onClick = onClick
                )
            }
            CountrySelectingButton(
                isLoading = state.isLoading,
                onClick = onButtonClick
            )
        }
    }
}

@Preview(name = "Standard Preview", widthDp = 300, heightDp = 300)
@Composable
fun CountrySelectingPagePreview() {
    CountrySelectingPage(
        state = CountrySelectingViewModel.State.Loaded(listOf(Continent("North America", countries = listOf(Country("ca")))))
    )
}
