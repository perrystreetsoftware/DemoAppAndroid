package com.example.feature.countryselecting.componenets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainmodels.Continent
import com.example.domainmodels.Country

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountrySelectingList(
    list: List<Continent>,
    onClick: ((Country) -> Unit)? = null
) {
    LazyColumn(modifier = Modifier.padding(10.dp)
    ) {
        list.forEach { continent ->
            stickyHeader {
                Text(text = continent.name, style = MaterialTheme.typography.h5,)
            }

            items(continent.countries) { item ->
                Row(modifier = Modifier.clickable {
                    onClick?.let { method ->
                        method(item)
                    }
                }.fillMaxWidth()
                    .padding(bottom = 10.dp)) {
                    Text(item.countryName)
                }
            }
        }
    }
}

@Preview
@Composable
fun CountrySelectingListPreview() {
    CountrySelectingList(list = listOf(
            Continent(
                name = "North America",
                countries = listOf(Country(regionCode = "us"))
            ),
            Continent(
                name = "Asia",
                countries = listOf(Country(regionCode = "ru"))
            ),
        )
    )
}
