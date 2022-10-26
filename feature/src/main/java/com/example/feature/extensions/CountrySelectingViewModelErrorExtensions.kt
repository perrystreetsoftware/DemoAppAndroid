package com.example.feature.extensions

import com.example.features.R
import com.example.uicomponents.models.UIError
import com.example.viewmodels.CountryListViewModelError

fun CountryListViewModelError.asUIError(): UIError {
    return when(this) {
        is CountryListViewModelError.Forbidden -> {
            UIError(R.string.forbidden_error_title, listOf(R.string.forbidden_error_message))
        }
        is CountryListViewModelError.Unknown -> {
            UIError(R.string.generic_error_title, listOf(R.string.generic_error_message))
        }
        is CountryListViewModelError.ConnectionError -> {
            UIError(R.string.connection_error_title, listOf(R.string.connection_error_message1, R.string.connection_error_message2))
        }
    }
}