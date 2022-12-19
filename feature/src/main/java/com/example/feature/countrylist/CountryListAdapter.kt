package com.example.feature.countrylist

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.domainmodels.Country
import com.example.feature.countrylist.error.asDialogState
import com.example.feature.countrylist.error.asToastState
import com.example.uicomponents.dialog.PssDialog
import com.example.viewmodels.CountryListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryListAdapter(
    viewModel: CountryListViewModel = getViewModel(),
    onCountrySelected: (Country) -> Unit,
) {
    val state by viewModel.state.subscribeAsState(CountryListViewModel.UiState())

    LaunchedEffect(state.navigationTarget) {
        state.navigationTarget?.let { onCountrySelected(it) }
        viewModel.resetNavigationTarget()
    }

    CountryListPage(
        listUiState = state,
        onCountrySelected = { country ->
            viewModel.onCountryTapped(country)
        },
        onRefreshTapped = { viewModel.onRefreshTapped() },
        onDismissBanner = { viewModel.dismissPersistentError() }
    )

    state.error?.asDialogState(
        goToRandomAction = { viewModel.navigateToRandomCountry() }
    )?.let { dialogState ->
        PssDialog(dialogState = dialogState, onDismissRequest = { viewModel.dismissError() })
    }

    state.error?.asToastState(LocalContext.current)?.let { toastState ->
        Toast.makeText(LocalContext.current, toastState.message, Toast.LENGTH_SHORT).show()
        viewModel.dismissError()
    }
}
