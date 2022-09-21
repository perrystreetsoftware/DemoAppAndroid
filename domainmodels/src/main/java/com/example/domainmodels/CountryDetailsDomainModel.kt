package com.example.domainmodels

data class CountryDetailsDomainModel(val countryName: String, val regionCode: String, val detailsText: String) {
    constructor(countryDetailsDTO: CountryDetailsDTO) : this(countryDetailsDTO.regionName, countryDetailsDTO.regionCode, countryDetailsDTO.legalCodeBody)

    companion object {
        val EMPTY = CountryDetailsDomainModel("", "", "")
    }
}