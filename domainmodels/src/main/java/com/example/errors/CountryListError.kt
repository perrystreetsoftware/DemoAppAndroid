package com.example.errors

sealed class CountryListError : Throwable() {
    object Forbidden : CountryListError()
    object ConnectionError : CountryListError()
    object Other : CountryListError()
    object UserNotLoggedIn : CountryListError()

    object NotEnoughPermissionsError : CountryListError()
    object NotAvailableError : CountryListError()
    data class BlockedCountry(val reason: String) : CountryListError()

    object InternalError : CountryListError()
}