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

interface TravelAdvisoriesDestination {
    val route: String
}

object CountriesDestination : TravelAdvisoriesDestination {
    override val route = "countries"
}

object CountryDestination : TravelAdvisoriesDestination {
    override val route = "country"
    const val regionCodeArg = "region_code"
}

@Composable
fun TravelAdvisoriesNavHost(navController: NavHostController = rememberNavController()) {
    val onCountrySelected: (Country) -> Unit = {
        navController.navigate("${CountryDestination.route}/${it.regionCode}")
    }

    NavHost(
        navController = navController,
        startDestination = CountriesDestination.route,
    ) {
        composable(route = CountriesDestination.route) {
            CountrySelectingAdapter(
                onCountrySelected = onCountrySelected
            )
        }
        composable(
             route = "${CountryDestination.route}/{${CountryDestination.regionCodeArg}}",
            arguments = listOf(navArgument(CountryDestination.regionCodeArg) {
                type = NavType.StringType
            })
        ) {
            CountryDetailsAdapter(
                regionCode =  it.arguments?.getString(CountryDestination.regionCodeArg) ?: return@composable
            )
        }
    }
}