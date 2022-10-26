package com.example.feature.countrydetails.component

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CountryDetailsContent(
    countryName: String,
    detailsText: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        Text(text = countryName, style = MaterialTheme.typography.h3, modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.size(16.dp))
        Text(modifier = Modifier.padding(10.dp), text = detailsText ?: "")
    }
}


@Preview
@Composable
fun CountryDetailsContentPreview() {
    CountryDetailsContent(countryName = "Test", detailsText = "Now is the time for all good men to come to the aid of their country.")
}
