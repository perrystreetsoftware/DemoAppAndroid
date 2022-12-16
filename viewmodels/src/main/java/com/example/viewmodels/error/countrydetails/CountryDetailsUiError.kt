package com.example.viewmodels.error.countrydetails

sealed class CountryDetailsUiError
sealed class CountryDetailsFullScreenError : CountryDetailsUiError() {
    object CountryNotFound : CountryDetailsFullScreenError()
    data class Generic(val message: String?) : CountryDetailsFullScreenError()
}