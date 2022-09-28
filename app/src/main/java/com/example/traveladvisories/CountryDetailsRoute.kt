package com.example.traveladvisories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.domainmodels.CountryDetails
import com.example.uicomponents.countrydetails.CountryDetailsPage
import com.example.uicomponents.countrydetails.CountryDetailsUIState
import com.example.viewmodels.CountryDetailsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryDetailsRoute(
    viewModel: CountryDetailsViewModel = getViewModel(),
    regionCode: String,
) {
    val viewModelState by viewModel.state.subscribeAsState(initial = CountryDetailsViewModel.State.Initial)
    val details by viewModel.details.subscribeAsState(initial = CountryDetails.EMPTY)

    CountryDetailsPage(
        detailsUIState = CountryDetailsUIState(
            details = details,
            viewModelState = viewModelState
        )
    )

    LaunchedEffect(regionCode) {
        viewModel.onPageLoaded(regionCode)
    }
}