package com.example.uicomponents.countrydetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.uicomponents.countrydetails.component.CountryDetailsContent
import com.example.uicomponents.countrydetails.component.CountryNotFoundErrorView
import com.example.uicomponents.countryselecting.CountrySelectingPage
import com.example.uicomponents.countryselecting.CountrySelectingUIState
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountryDetailsViewModel
import com.example.viewmodels.CountryDetailsViewModelError
import com.example.viewmodels.CountrySelectingViewModel

@Composable
fun CountryDetailsPage(
    detailsUIState: CountryDetailsUIState,
) {
    ProgressIndicator(isLoading = detailsUIState.viewModelState.isLoading)
    CountryNotFoundErrorView(state = detailsUIState.viewModelState)
    CountryDetailsContent(
        countryName = detailsUIState.details.country.countryName,
        detailsText = detailsUIState.details.detailsText
    )
}


@Preview
@Composable
fun CountryDetailsPagePreview() {
    CountryDetailsPage(
        detailsUIState = CountryDetailsUIState(
            details = CountryDetails(
                country = Country(
                    regionCode = "us"
                ),
                detailsText = "Now is the time for all good men to come to the aid of their country."
            ),
            viewModelState = CountryDetailsViewModel.State.Error(CountryDetailsViewModelError.CountryNotFound)
        )
    )
}
