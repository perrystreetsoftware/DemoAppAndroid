package com.example.errors

sealed class CountryListError : Throwable() {
    object Forbidden : CountryListError()
    object ConnectionError : CountryListError()
    object Other : CountryListError()
    object UserNotLoggedIn : CountryListError()

    object NotEnoughPermissionsError : CountryListError()
    object NotAvailableError : CountryListError()
    object BlockedCountry : CountryListError()

    object InternalError : CountryListError()
}