package com.example.viewmodels.error.countrydetails

import com.example.errors.CountryDetailsError
import com.example.viewmodels.error.UiErrorMapper

object CountryDetailsUiErrorMapper : UiErrorMapper<CountryDetailsError, CountryDetailsUiError> {

    override fun toUiError(error: CountryDetailsError): CountryDetailsUiError {
        return when (error) {
            CountryDetailsError.CountryNotFound -> CountryDetailsFullScreenError.CountryNotFound
            is CountryDetailsError.Other -> CountryDetailsFullScreenError.Generic(error.message)
        }
    }
}

fun CountryDetailsError.toUiError(): CountryDetailsUiError = CountryDetailsUiErrorMapper.toUiError(this)