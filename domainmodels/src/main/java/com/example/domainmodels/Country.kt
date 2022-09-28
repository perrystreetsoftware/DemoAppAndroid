package com.example.domainmodels

import java.util.*

data class Country( val regionCode: String) {
    val countryName: String
        get() = Locale("", regionCode).displayCountry
}
