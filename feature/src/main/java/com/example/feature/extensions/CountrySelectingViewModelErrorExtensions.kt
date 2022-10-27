package com.example.feature.extensions

import com.example.errors.CountryListError
import com.example.features.R
import com.example.uicomponents.models.UIError

fun CountryListError.asUIError(): UIError {
    return when(this) {
        is CountryListError.Forbidden -> {
            UIError(R.string.forbidden_error_title, listOf(R.string.forbidden_error_message))
        }
        is CountryListError.Other -> {
            UIError(R.string.generic_error_title, listOf(R.string.generic_error_message))
        }
        is CountryListError.ConnectionError -> {
            UIError(R.string.connection_error_title, listOf(R.string.connection_error_message1, R.string.connection_error_message2))
        }
        is CountryListError.UserNotLoggedIn -> {
            UIError(R.string.generic_error_title, listOf(R.string.generic_error_message))
        }
    }
}