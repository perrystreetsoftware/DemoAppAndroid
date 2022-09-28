package com.example.traveladvisories

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature.countrydetails.CountryDetailsAdapter
import com.example.feature.countryselecting.CountrySelectingAdapter
import com.example.domainmodels.Country

sealed class TravelAdvisoriesDestination(val route: String) {
    object CountriesDestination : TravelAdvisoriesDestination("countries")
    object CountryDestination : TravelAdvisoriesDestination("country") {
        const val regionCodeArg = "region_code"
    }
}

@Composable
fun TravelAdvisoriesNavHost(navController: NavHostController = rememberNavController()) {
    val onCountrySelected: (Country) -> Unit = {
        navController.navigate("${TravelAdvisoriesDestination.CountryDestination.route}/${it.regionCode}")
    }

    NavHost(
        navController = navController,
        startDestination = TravelAdvisoriesDestination.CountriesDestination.route,
    ) {
        composable(route = TravelAdvisoriesDestination.CountriesDestination.route) {
            CountrySelectingAdapter(
                onCountrySelected = onCountrySelected
            )
        }
        composable(
            route = "${TravelAdvisoriesDestination.CountryDestination.route}/{${TravelAdvisoriesDestination.CountryDestination.regionCodeArg}}",
            arguments = listOf(navArgument(TravelAdvisoriesDestination.CountryDestination.regionCodeArg) {
                type = NavType.StringType
            })
        ) {
            CountryDetailsAdapter(
                regionCode = it.arguments?.getString(TravelAdvisoriesDestination.CountryDestination.regionCodeArg) ?: return@composable
            )
        }
    }
}