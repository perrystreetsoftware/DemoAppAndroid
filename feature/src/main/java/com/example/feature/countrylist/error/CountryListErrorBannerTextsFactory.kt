package com.example.feature.countrylist.error

import android.content.Context
import com.example.features.R
import com.example.uicomponents.error.ErrorBannerFactory

class CountryListErrorBannerTextsFactory(private val context: Context) : ErrorBannerFactory<CountryListBannerError> {

    override fun getTitleAndMessage(error: CountryListBannerError): Pair<String, String> {
        return when (error) {
            CountryListBannerError.NoPermissions -> {
                context.getString(R.string.country_list_no_permissions_error_title) to
                    context.getString(R.string.country_list_no_permissions_error_message)
            }
        }
    }
}

fun CountryListBannerError.titleAndMessage(context: Context): Pair<String, String> =
    CountryListErrorBannerTextsFactory(context).getTitleAndMessage(this)