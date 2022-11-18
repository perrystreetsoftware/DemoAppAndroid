package com.example.feature.countrylist

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import com.example.domainmodels.Country
import com.example.errors.CountryListError
import com.example.features.R
import com.example.viewmodels.CountryListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryListAdapter(
    viewModel: CountryListViewModel = getViewModel(),
    onCountrySelected: (Country) -> Unit
) {
    val state by viewModel.state.subscribeAsState(CountryListViewModel.UiState())
    CountryListPage(
        listUiState = state,
        onCountrySelected = { country -> onCountrySelected(country) },
        onRefreshTapped = { viewModel.onRefreshTapped() },
    )

    if (state.error != null) {
        AlertDialog(
            error = state.error!!,
            onDismiss = { viewModel.onErrorDismissed() }
        )
    }
}


@Composable
fun AlertDialog(error: CountryListError, onDismiss: () -> Unit) {
    return when(error) {
        is CountryListError.Forbidden -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(id = R.string.forbidden_error_title)) },
                text = {
                    Text(text = stringResource(id = R.string.forbidden_error_message))
                },
                confirmButton = { },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }
        is CountryListError.Other, CountryListError.UserNotLoggedIn -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(id = R.string.forbidden_error_title)) },
                text = {
                    Text(text = stringResource(id = R.string.generic_error_message))
                },
                confirmButton = { },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }
        is CountryListError.ConnectionError -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(id = R.string.connection_error_title)) },
                text = {
                    Text(text = stringResource(id = R.string.connection_error_message1))
                },
                confirmButton = { },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                }
            )
        }
    }
}