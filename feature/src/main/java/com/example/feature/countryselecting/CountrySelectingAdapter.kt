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
    val errorDialogState = remember { mutableStateOf<UIError?>(null) }
    ErrorDialog(
        state = errorDialogState.value,
        onDismiss = { viewModel.onErrorDismissed() }
    )

    CountrySelectingEventsAdapter(
        viewModel = viewModel,
        onCountrySelected = onCountrySelected,
        errorDialogState = errorDialogState
    )
}

@Composable
fun ErrorDialog(state: UIError?, onDismiss: () -> Unit) {
    if (state != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = stringResource(id = state.titleKey)) },
            text = {
                Text(text = state.messageKeys
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

@Composable
private fun CountrySelectingEventsAdapter(
    viewModel: CountrySelectingViewModel,
    onCountrySelected: (String) -> Unit,
    errorDialogState: MutableState<UIError?>
) {
    val events by viewModel.events.subscribeAsState(CountrySelectingViewModel.Event.Appear)
    LaunchedEffect(events) { // will be called only if events value has changed
        when (val event = events) {
            is CountrySelectingViewModel.Event.Appear -> {
                viewModel.onPageLoaded()
            }
            is CountrySelectingViewModel.Event.ErrorAppear -> {
                errorDialogState.value = event.error.asUIError()
            }
            is CountrySelectingViewModel.Event.ErrorDisappear -> {
                errorDialogState.value = null
            }
            is CountrySelectingViewModel.Event.CountrySelect -> {
                onCountrySelected.invoke(event.domainModel.regionCode)
            }
        }
    }
}