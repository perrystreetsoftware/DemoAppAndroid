package com.example.feature.countrylist.componenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.features.R

@Composable
fun CountryListButton(
    isLoaded: Boolean,
    onClick: (() -> Unit)? = null
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            onClick?.let {
                it()
            }
        }, enabled = isLoaded
    ) {
        Text(text = stringResource(id = R.string.refresh_button_title))
    }
}
