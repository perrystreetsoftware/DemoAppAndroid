package com.example.feature.countrylist

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.domainmodels.Country
import com.example.feature.countrylist.error.dialogState
import com.example.feature.countrylist.error.toastMessage
import com.example.uicomponents.dialog.PssDialog
import com.example.viewmodels.error.countrylist.CountryListDialogError
import com.example.viewmodels.error.countrylist.CountryListToastError
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
        onDismissBannerError = { viewModel.dismissPersistentError() }
    )

    state.error?.let { uiError ->
        when (uiError) {
            is CountryListToastError -> {
                Toast.makeText(LocalContext.current, uiError.toastMessage(), Toast.LENGTH_SHORT).show()
                viewModel.dismissError()
            }
            is CountryListDialogError -> {
                PssDialog(config = uiError.dialogState(
                    goToRandomAction = { viewModel.navigateToRandomCountry() }),
                    onDismissRequest = { viewModel.dismissError() })
            }
            else -> {}
        }
    }

}
