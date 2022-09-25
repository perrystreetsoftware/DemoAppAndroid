package com.example.extensions

import com.example.libs.UIError
import com.example.traveladvisories.R
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