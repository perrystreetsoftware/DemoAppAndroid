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
    NavHost(
        navController = navController,
        startDestination = Countries.route,
    ) {
        composable(route = Countries.route) {
            CountrySelectingAdapter(
                onCountrySelected = {
                    navController.navigate("${Country.route}/$it")
                })
        }
        composable(
            route = "${Country.route}/{${Country.regionCodeArg}}",
            arguments = listOf(navArgument(Country.regionCodeArg) {
                type = NavType.StringType
            })
        ) {
            CountryDetailsAdapter(
                regionCode = it.arguments?.getString(Country.regionCodeArg) ?: return@composable
            )
        }
    }
}