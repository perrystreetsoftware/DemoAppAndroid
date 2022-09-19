package com.example.uicomponents.countryselecting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainmodels.ContinentUIModel
import com.example.domainmodels.CountryUIModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountrySelectingList(
    list: List<ContinentUIModel>
) {
    LazyColumn(
    ) {
        list.forEach { continent ->
            stickyHeader {
                Text(continent.name)
            }

            items(continent.countries) { item ->
                Text(item.countryName)
            }
        }
    }
}

@Preview
@Composable
fun CountrySelectingListPreview() {
    CountrySelectingList(list = listOf(
            ContinentUIModel(
                name = "Europe",
                countries = listOf(CountryUIModel(countryName = "Uganda", regionCode = "ug"))
            ),
            ContinentUIModel(
                name = "Asia",
                countries = listOf(CountryUIModel(countryName = "Russia", regionCode = "ru"))
            ),
        )
    )
}
