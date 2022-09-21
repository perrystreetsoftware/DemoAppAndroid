package com.example.domainmodels

import java.io.Serializable
import java.util.*

data class Country( val regionCode: String) : Serializable {
    val countryName: String
        get() = Locale("", regionCode).displayCountry
}
