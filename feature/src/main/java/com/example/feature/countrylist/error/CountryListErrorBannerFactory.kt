package com.example.feature.countrylist.error

import android.content.Context
import com.example.errors.CountryListError
import com.example.features.R
import com.example.uicomponents.error.ErrorBannerFactory
import com.example.uicomponents.models.BannerState

class CountryListErrorBannerFactory(private val context: Context) : ErrorBannerFactory<CountryListError> {

    override fun getBannerState(error: CountryListError): BannerState? {
        return when (error) {
            CountryListError.NotEnoughPermissionsError -> {
                BannerState(title = context.getString(R.string.country_list_no_permissions_error_title),
                    message = context.getString(R.string.country_list_no_permissions_error_message))
            }
            else -> null
        }
    }
}

fun CountryListError.asBannerState(context: Context): BannerState? =
    CountryListErrorBannerFactory(context).getBannerState(this)