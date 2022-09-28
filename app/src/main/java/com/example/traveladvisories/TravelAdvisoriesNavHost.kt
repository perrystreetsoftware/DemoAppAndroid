package com.example.traveladvisories

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

interface TravelAdvisoriesDestination {
    val route: String
}

object Countries : TravelAdvisoriesDestination {
    override val route = "countries"
}

object Country : TravelAdvisoriesDestination {
    override val route = "country"
    const val regionCodeArg = "region_code"
}

@Composable
fun TravelAdvisoriesNavHost(navController: NavHostController = rememberNavController()) {
    val navigateToCountryDetails: (String) -> Unit = { navController.navigate("${Country.route}/$it") }
    NavHost(
        navController = navController,
        startDestination = Countries.route,
    ) {
        composable(route = Countries.route) {
            CountrySelectingRoute(onCountrySelected = navigateToCountryDetails)
        }
        composable(
            route = "${Country.route}/{${Country.regionCodeArg}}",
            arguments = listOf(navArgument(Country.regionCodeArg) {
                type = NavType.StringType
            })
        ) {
            CountryDetailsRoute(
                 regionCode = it.arguments?.getString(Country.regionCodeArg) ?: return@composable
            )
        }
    }
}