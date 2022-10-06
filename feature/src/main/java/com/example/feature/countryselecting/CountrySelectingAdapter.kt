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
    val errorState = remember { mutableStateOf<UIError?>(null) }
    val events by viewModel.events.subscribeAsState(Unit)

    CountrySelectingPage(
        state = state,
        onClick = { country -> viewModel.onCountrySelected(country) },
        onButtonClick = { viewModel.onButtonTapped() },
    )
    ErrorDialog(
        state = errorState.value,
        onDismiss = { viewModel.onErrorDismissed() }
    )

    when (val event = events) {
        is CountrySelectingViewModel.Event.Navigate -> {
            onCountrySelected.invoke(event.domainModel.regionCode)
        }
        is CountrySelectingViewModel.Event.Error -> {
            errorState.value = event.error.asUIError()
        }
        is CountrySelectingViewModel.Event.ErrorDisappear -> {
            errorState.value = null
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onPageLoaded()
    }
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
