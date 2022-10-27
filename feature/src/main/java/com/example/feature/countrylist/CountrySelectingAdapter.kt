package com.example.feature.countrylist

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import com.example.domainmodels.Country
import com.example.errors.CountryListError
import com.example.feature.extensions.asUIError
import com.example.viewmodels.CountryListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountryListAdapter(
    viewModel: CountryListViewModel = getViewModel(),
    onCountrySelected: (Country) -> Unit
) {
    val state by viewModel.state.subscribeAsState(CountryListViewModel.UiState())
    CountryListPage(
        state = state,
        onCountrySelected = { country -> onCountrySelected(country) },
        onRefreshTapped = { viewModel.onRefreshTapped() },
    )

    ErrorDialog(
        state = state.error,
        onDismiss = { viewModel.onErrorDismissed() }
    )
}

@Composable
fun ErrorDialog(state: CountryListError?, onDismiss: () -> Unit) {
    if (state != null) {
        (state!!.asUIError()).let { uiError ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(id = uiError.titleKey)) },
                text = {
                    Text(text = uiError.messageKeys
                        .mapIndexed { _, it -> stringResource(id = it) }
                        .joinToString(" ")
                    )
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
