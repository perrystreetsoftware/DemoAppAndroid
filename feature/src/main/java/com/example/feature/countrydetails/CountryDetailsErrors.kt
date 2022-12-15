package com.example.feature.countrydetails

import android.content.Context
import com.example.errors.CountryDetailsError
import com.example.features.R
import com.example.uicomponents.error.UiErrorMapper

sealed class CountryDetailsUiError
sealed class CountryDetailsFullScreenError : CountryDetailsUiError() {
    object CountryNotFound : CountryDetailsFullScreenError()
    data class Generic(val message: String?) : CountryDetailsFullScreenError()
}

object CountryDetailsUiErrorMapper : UiErrorMapper<CountryDetailsError, CountryDetailsUiError> {

    override fun toUiError(error: CountryDetailsError): CountryDetailsUiError {
        return when (error) {
            CountryDetailsError.CountryNotFound -> CountryDetailsFullScreenError.CountryNotFound
            is CountryDetailsError.Other -> CountryDetailsFullScreenError.Generic(error.message)
        }
    }
}

class CountryDetailsFullScreenErrorMessageFactory(private val context: Context) {

    fun getMessage(error: CountryDetailsFullScreenError) = when (error) {
        CountryDetailsFullScreenError.CountryNotFound -> context.getString(R.string.country_not_found_error_message)
        is CountryDetailsFullScreenError.Generic -> {
            error.message ?: context.getString(R.string.generic_error_message)
        }
    }
}

fun CountryDetailsError.toUiError(): CountryDetailsUiError = CountryDetailsUiErrorMapper.toUiError(this)
fun CountryDetailsFullScreenError.message(context: Context): String =
    CountryDetailsFullScreenErrorMessageFactory(context).getMessage(this)