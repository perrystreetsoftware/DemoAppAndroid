package com.example.feature.countrylist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.domainmodels.Country
import com.example.feature.countrylist.componenets.AlertNotifier
import com.example.feature.countrylist.error.asFloatingAlert
import com.example.viewmodels.CountryListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryListAdapter(
    viewModel: CountryListViewModel = getViewModel(),
    onCountrySelected: (Country) -> Unit,
    onAboutSelected: () -> Unit
) {
    val state by viewModel.state.subscribeAsState(CountryListViewModel.UiState())

    LaunchedEffect(state.navigationTarget) {
        state.navigationTarget?.let { onCountrySelected(it) }
        viewModel.resetNavigationTarget()
    }

    CountryListPage(
        listUiState = state,
        onCountrySelected = { country ->
            viewModel.onCountrySelected(country)
        },
        onRefreshTapped = { viewModel.onRefreshTapped() },
        onFailOtherTapped = { viewModel.onFailOtherTapped() }
    )

    AlertNotifier(
        floatingAlert = state.error?.asFloatingAlert(viewModel, onAboutSelected),
        errorDismissing = viewModel
    )
}
