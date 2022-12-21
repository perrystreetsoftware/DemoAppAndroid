package com.example.feature.countrydetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.feature.countrydetails.component.CountryDetailsContent
import com.example.feature.countrydetails.component.CountryNotFoundErrorView
import com.example.feature.countrydetails.component.FullScreenErrorView
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountryDetailsViewModel

@Composable
fun CountryDetailsPage(detailsUIState: CountryDetailsViewModel.UiState) {
    ProgressIndicator(isLoading = detailsUIState is CountryDetailsViewModel.UiState.Loading)
    CountryNotFoundErrorView(detailsUIState = detailsUIState)
    CountryDetailsContent(
        countryName = (detailsUIState as? CountryDetailsViewModel.UiState.Loaded)?.details?.country?.countryName ?: "",
        detailsText = (detailsUIState as? CountryDetailsViewModel.UiState.Loaded)?.details?.detailsText ?: "",
    )
}


@Preview
@Composable
fun CountryDetailsPagePreview() {
    CountryDetailsPage(
        detailsUIState = CountryDetailsViewModel.UiState.Loaded(
            CountryDetails(
                country = Country(
                    regionCode = "us"
                ),
                detailsText = "Now is the time for all good men to come to the aid of their country."
            )
        )
    )
}
