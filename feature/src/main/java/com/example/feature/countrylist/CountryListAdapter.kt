package com.example.feature.countrylist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.domainmodels.Country
import com.example.feature.countrylist.error.asFloatingAlert
import com.example.uicomponents.library.FloatingAlertNotifier
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
        onCountrySelect = { country ->
            viewModel.onCountrySelect(country)
        },
        onRefreshTap = { viewModel.onRefreshTap() },
        onFailOtherTap = { viewModel.onFailOtherTap() }
    )

    FloatingAlertNotifier(
        floatingAlert = state.error?.asFloatingAlert(viewModel, onAboutSelected),
        errorDismissing = { viewModel.dismissError() }
    )
}
