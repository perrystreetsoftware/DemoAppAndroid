package com.example.feature.countryselecting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.feature.countryselecting.componenets.CountrySelectingButton
import com.example.feature.countryselecting.componenets.CountrySelectingList
import com.example.uicomponents.library.ProgressIndicator
import com.example.viewmodels.CountrySelectingViewModel
import com.example.features.R

@Composable
fun CountrySelectingPage(
    state: CountrySelectingViewModel.UiState,
    onCountrySelected: ((Country) -> Unit)? = null,
    onRefreshTapped: (() -> Unit)? = null
) {
    Box {
        ProgressIndicator(isLoading = state.isLoading)
        Column(modifier = Modifier.fillMaxHeight()) {
            Column(modifier = Modifier.weight(1f)) {
                CountrySelectingList(
                    list = state.continents,
                    onClick = onCountrySelected
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.serverStatus?.success == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.server_status_ok))
                        Spacer(Modifier.size(10.dp))
                        CircleShape(color = Color.Green)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.server_status_not_ok))
                        Spacer(Modifier.size(10.dp))
                        CircleShape(color = Color.Yellow)
                    }
                }
            }
            CountrySelectingButton(
                isLoaded = state.isLoaded,
                onClick = onRefreshTapped
            )
        }
    }
}

@Composable
fun CircleShape(color: Color) {
    Box(
        modifier = Modifier
            .size(size = 15.dp)
            .clip(shape = RoundedCornerShape(50))
            .background(color = color)
    ) {
    }
}

@Preview(name = "Standard Preview", widthDp = 300, heightDp = 300)
@Composable
fun CountrySelectingPagePreview() {
    CountrySelectingPage(
        state = CountrySelectingViewModel.UiState(continents = listOf(Continent("North America", countries = listOf(Country("ca")))))
    )
}
