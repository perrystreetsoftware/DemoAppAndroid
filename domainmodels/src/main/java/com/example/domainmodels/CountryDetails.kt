package com.example.domainmodels

data class CountryDetails(val country: Country, val detailsText: String?) {
    companion object {
        val EMPTY = CountryDetails(Country(regionCode = ""), "")
    }
}