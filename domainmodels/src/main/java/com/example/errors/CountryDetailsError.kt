package com.example.errors

sealed class CountryDetailsError: Throwable() {
    object CountryNotFound: CountryDetailsError()
    data class Other(val throwable: Throwable): CountryDetailsError()
}
