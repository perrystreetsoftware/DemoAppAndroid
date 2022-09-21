package com.example.uicomponents.countryselecting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.viewmodels.CountrySelectingViewModel

class CountrySelectingAdapter(private val viewModel: CountrySelectingViewModel) {
    @Composable
    fun Content() {
        val state by viewModel.state.subscribeAsState(CountrySelectingViewModel.State.Initial)
        val continents by viewModel.continents.subscribeAsState(initial = emptyList())

        CountrySelectingPage(
            state = CountrySelectingUIState(
                continents = continents,
                state = state
            ),
            onClick = { country ->
                viewModel.onCountrySelected(country)
            }
        )
    }
}