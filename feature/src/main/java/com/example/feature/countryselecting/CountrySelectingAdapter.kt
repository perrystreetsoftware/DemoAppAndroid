package com.example.feature.countryselecting

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import com.example.feature.extensions.asUIError
import com.example.uicomponents.models.UIError
import com.example.viewmodels.CountrySelectingViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CountrySelectingAdapter(
    viewModel: CountrySelectingViewModel = getViewModel(),
    onCountrySelected: (String) -> Unit
) {
    val state by viewModel.state.subscribeAsState(CountrySelectingViewModel.State.Initial)
    CountrySelectingPage(
        state = state,
        onClick = { country -> viewModel.onCountrySelected(country) },
        onButtonClick = { viewModel.onButtonTapped() },
    )

    val errorState by viewModel.errorState.subscribeAsState(CountrySelectingViewModel.ErrorState.None)
    ErrorDialog(
        state = errorState,
        onDismiss = { viewModel.onErrorDismissed() }
    )

    LaunchedEffect(Unit) {
        viewModel.onPageLoaded()
    }
}

@Composable
fun ErrorDialog(state: CountrySelectingViewModel.ErrorState?, onDismiss: () -> Unit) {
    if (state is CountrySelectingViewModel.ErrorState.Error) {
        (state.throwable.asUIError()).let { uiError ->
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = stringResource(id = uiError.titleKey)) },
                text = {
                    Text(text = uiError.messageKeys
                        .mapIndexed { _, it -> stringResource(id = it) }
                        .joinToString()
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
