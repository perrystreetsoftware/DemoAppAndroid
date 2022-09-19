package com.example.uicomponents.countryselecting

import androidx.compose.runtime.Composable
import com.example.viewmodels.CountrySelectingViewModel
import androidx.compose.runtime.rxjava3.subscribeAsState
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class CountrySelectingAdapter(private val viewModel: CountrySelectingViewModel) {
    @Composable
    fun Content(
    ) {
        val stateStream = viewModel.state.subscribeAsState(CountrySelectingViewModel.State.Initial)
        val continentsStream = viewModel.continents.subscribeAsState(initial = emptyList())

        stateStream?.value.let { state ->
            continentsStream.value?.let { continents ->
                CountrySelectingPage(
                    state = CountrySelectingUIState(
                        continents = continents,
                        state = state ?: CountrySelectingViewModel.State.Initial
                    )
                )
            }
        }
    }
}