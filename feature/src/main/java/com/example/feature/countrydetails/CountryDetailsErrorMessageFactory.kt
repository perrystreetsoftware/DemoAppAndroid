package com.example.feature.countrydetails

import android.content.Context
import com.example.errors.CountryDetailsError
import com.example.features.R
import com.example.uicomponents.error.CustomErrorStateFactory

class CountryDetailsErrorMessageFactory(private val context: Context) :
    CustomErrorStateFactory<CountryDetailsError, String> {

    override fun getErrorDisplayState(error: CountryDetailsError): String = when (error) {
        CountryDetailsError.CountryNotFound -> context.getString(R.string.country_not_found_error_message)
        is CountryDetailsError.Other -> {
            error.message ?: context.getString(R.string.generic_error_message)
        }
    }
}

fun CountryDetailsError.asFullscreenErrorState(context: Context): String =
    CountryDetailsErrorMessageFactory(context).getErrorDisplayState(this)
