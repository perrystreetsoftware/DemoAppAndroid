package com.example.feature.extensions

import com.example.features.R
import com.example.uicomponents.models.UIError
import com.example.viewmodels.CountrySelectingViewModelError

fun CountrySelectingViewModelError.asUIError(): UIError {
    return when(this) {
        is CountrySelectingViewModelError.Forbidden -> {
            UIError(R.string.forbidden_error_title, listOf(R.string.forbidden_error_message))
        }
        is CountrySelectingViewModelError.Unknown -> {
            UIError(R.string.generic_error_title, listOf(R.string.generic_error_message))
        }
    }
}