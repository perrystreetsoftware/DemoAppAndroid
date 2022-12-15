package com.example.feature.countrylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainmodels.Continent
import com.example.domainmodels.Country
import com.example.feature.countrylist.componenets.CountryListButton
import com.example.feature.countrylist.componenets.CountryListList
import com.example.feature.countrylist.error.CountryListBannerError
import com.example.feature.countrylist.error.titleAndMessage
import com.example.feature.countrylist.error.toUiError
import com.example.features.R
import com.example.uicomponents.library.ProgressIndicator
import com.example.uicomponents.library.SimpleBanner
import com.example.viewmodels.CountryListViewModel

@Composable
fun CountryListPage(
    listUiState: CountryListViewModel.UiState,
    onCountrySelected: ((Country) -> Unit)? = null,
    onRefreshTapped: (() -> Unit)? = null,
    onDismissBannerError: () -> Unit,
) {
    Box {
        ProgressIndicator(isLoading = listUiState.isLoading)
        Column(modifier = Modifier.fillMaxHeight()) {
            listUiState.persistentError?.toUiError()?.let { uiError ->
                if (uiError is CountryListBannerError) {
                    val (title, message) = uiError.titleAndMessage(LocalContext.current)
                    Box(modifier = Modifier.padding(8.dp)) {
                        SimpleBanner(title, message, onDismissBannerError)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                CountryListList(
                    list = listUiState.continents,
                    onClick = onCountrySelected
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (listUiState.serverStatus?.success == true) {
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
            CountryListButton(
                isLoaded = listUiState.isLoaded,
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
fun CountryListPagePreview() {
    CountryListPage(
        listUiState = CountryListViewModel.UiState(
            continents = listOf(
                Continent(
                    name = "North America",
                    countries = listOf(Country("ca"))
                )
            )
        ),
        onDismissBannerError = {}
    )
}
