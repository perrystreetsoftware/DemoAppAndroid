package com.example.uicomponents.countryselecting.componenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CountrySelectingButton(
    isLoading: Boolean,
    onClick: (() -> Unit)? = null
) {
    if (!isLoading) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = {
            onClick?.let {
                it()
            }
        }) {
            Text(text = "Failing Api Call")
        }
    }
}
