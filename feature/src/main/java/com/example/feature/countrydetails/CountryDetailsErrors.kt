package com.example.feature.countrydetails

import android.content.Context
import com.example.errors.CountryDetailsError
import com.example.features.R
import com.example.viewmodels.error.countrydetails.CountryDetailsFullScreenError
import com.example.viewmodels.error.countrydetails.CountryDetailsUiError
import com.example.viewmodels.error.countrydetails.CountryDetailsUiErrorMapper

class CountryDetailsFullScreenErrorMessageFactory(private val context: Context) {

    fun getMessage(error: CountryDetailsFullScreenError) = when (error) {
        CountryDetailsFullScreenError.CountryNotFound -> context.getString(R.string.country_not_found_error_message)
        is CountryDetailsFullScreenError.Generic -> {
            error.message ?: context.getString(R.string.generic_error_message)
        }
    }
}

fun CountryDetailsFullScreenError.message(context: Context): String =
    CountryDetailsFullScreenErrorMessageFactory(context).getMessage(this)