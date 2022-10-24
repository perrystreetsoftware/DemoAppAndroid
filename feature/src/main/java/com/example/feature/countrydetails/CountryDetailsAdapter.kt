package com.example.feature.countrydetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.viewmodels.CountryDetailsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryDetailsAdapter(
    viewModel: CountryDetailsViewModel = getViewModel(),
    regionCode: String,
) {
    val viewModelState by viewModel.state.subscribeAsState(initial = CountryDetailsViewModel.State.Initial)

    CountryDetailsPage(
        viewModelState
    )

    LaunchedEffect(regionCode) {
        viewModel.onPageLoaded(regionCode)
    }
}