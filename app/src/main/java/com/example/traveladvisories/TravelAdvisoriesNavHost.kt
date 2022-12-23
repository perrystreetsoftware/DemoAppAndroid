package com.example.traveladvisories

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature.countrydetails.CountryDetailsAdapter
import com.example.feature.countrylist.CountryListAdapter
import com.example.domainmodels.Country
import com.example.feature.about.AboutAdapter

sealed class TravelAdvisoriesDestination(val route: String) {
    object AboutDestination: TravelAdvisoriesDestination("about")
    object CountryListDestination : TravelAdvisoriesDestination("countries")
    object CountryDetailsDestination : TravelAdvisoriesDestination("country") {
        const val regionCodeArg = "region_code"
    }
}

@Composable
fun TravelAdvisoriesNavHost(navController: NavHostController = rememberNavController()) {
    val onCountrySelected: (Country) -> Unit = {
        navController.navigate("${TravelAdvisoriesDestination.CountryDetailsDestination.route}/${it.regionCode}")
    }
    val onAboutSelected = {
        navController.navigate("${TravelAdvisoriesDestination.AboutDestination.route}")
    }

    NavHost(
        navController = navController,
        startDestination = TravelAdvisoriesDestination.CountryListDestination.route,
    ) {
        composable(route = TravelAdvisoriesDestination.AboutDestination.route) {
            AboutAdapter()
        }
        composable(route = TravelAdvisoriesDestination.CountryListDestination.route) {
            CountryListAdapter(
                onCountrySelected = onCountrySelected,
                onAboutSelected = onAboutSelected
            )
        }
        composable(
            route = "${TravelAdvisoriesDestination.CountryDetailsDestination.route}/{${TravelAdvisoriesDestination.CountryDetailsDestination.regionCodeArg}}",
            arguments = listOf(navArgument(TravelAdvisoriesDestination.CountryDetailsDestination.regionCodeArg) {
                type = NavType.StringType
            })
        ) {
            CountryDetailsAdapter(
                regionCode = it.arguments?.getString(TravelAdvisoriesDestination.CountryDetailsDestination.regionCodeArg) ?: return@composable
            )
        }
    }
}