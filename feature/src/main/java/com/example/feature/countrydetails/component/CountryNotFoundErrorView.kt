package com.example.feature.countrydetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.viewmodels.CountryDetailsViewModel
import com.example.viewmodels.CountryDetailsViewModelError
import com.example.features.R

@Composable
fun CountryNotFoundErrorView(
    state: CountryDetailsViewModel.UiState,
) {
    if (state is CountryDetailsViewModel.UiState.Error) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Red)
                    .padding(10.dp),
            ) {
                Text(text = stringResource(id = R.string.country_not_found_error_title), style = MaterialTheme.typography.h4, color = Color.White)
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = stringResource(id = R.string.country_not_found_error_message), color = Color.White)
            }
        }
    }
}

@Preview(name = "Standard Preview", device = Devices.PIXEL, showSystemUi = true)
@Composable
fun CountryNotFoundErrorViewPreview() {
    CountryNotFoundErrorView(
        state = CountryDetailsViewModel.UiState.Error(error = CountryDetailsViewModelError.CountryNotFound)
    )
}