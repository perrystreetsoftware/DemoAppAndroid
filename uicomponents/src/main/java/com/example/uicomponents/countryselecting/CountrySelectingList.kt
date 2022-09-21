package com.example.uicomponents.countryselecting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainmodels.ContinentDomainModel
import com.example.domainmodels.CountryDomainModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountrySelectingList(
    list: List<ContinentDomainModel>,
    onClick: ((CountryDomainModel) -> Unit)? = null
) {
    LazyColumn(
    ) {
        list.forEach { continent ->
            stickyHeader {
                Text(continent.name)
            }

            items(continent.countries) { item ->
                Row(modifier = Modifier.clickable {
                    onClick?.let { method ->
                        method(item)
                    }
                }) {
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
            ContinentDomainModel(
                name = "Europe",
                countries = listOf(CountryDomainModel(countryName = "Uganda", regionCode = "ug"))
            ),
            ContinentDomainModel(
                name = "Asia",
                countries = listOf(CountryDomainModel(countryName = "Russia", regionCode = "ru"))
            ),
        )
    )
}
