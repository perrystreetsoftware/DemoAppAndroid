package com.example.domainmodels

data class CountryDetails(val country: Country, val detailsText: String?) {
    constructor(countryDetailsDTO: CountryDetailsDTO) : this(Country(regionCode = countryDetailsDTO.regionCode), countryDetailsDTO.legalCodeBody)

    companion object {
        val EMPTY = CountryDetails(Country(regionCode = ""), "")
    }
}