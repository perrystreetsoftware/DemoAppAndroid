package com.example.feature.countrydetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.viewmodels.CountryDetailsViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CountryDetailsAdapter(
    viewModel: CountryDetailsViewModel = getViewModel(parameters = { parametersOf(regionCode) }),
    regionCode: String,
) {
    val viewModelState by viewModel.state.subscribeAsState(initial = CountryDetailsViewModel.UiState.Initial)

    CountryDetailsPage(
        viewModelState
    )
}