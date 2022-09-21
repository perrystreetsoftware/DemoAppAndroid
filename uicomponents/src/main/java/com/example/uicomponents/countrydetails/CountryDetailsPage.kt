package com.example.uicomponents.countrydetails

import androidx.compose.runtime.Composable
import com.example.uicomponents.countrydetails.component.CountryDetailsContent
import com.example.uicomponents.library.ProgressIndicator

@Composable
fun CountryDetailsPage(
    detailsUIState: CountryDetailsUIState,
) {
    ProgressIndicator(isLoading = detailsUIState.viewModelState.isLoading)
    CountryDetailsContent(
        countryName = detailsUIState.details.country.countryName,
        detailsText = detailsUIState.details.detailsText
    )
}
