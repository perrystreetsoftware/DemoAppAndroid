package com.example.errors

sealed class CountryListError: Throwable() {
    object Forbidden: CountryListError()
    object ConnectionError: CountryListError()
    object Other: CountryListError()
    object UserNotLoggedIn: CountryListError()
}