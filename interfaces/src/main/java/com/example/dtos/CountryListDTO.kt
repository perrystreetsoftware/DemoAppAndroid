package com.example.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryListDTO(
    @Json(name = "africa")
    val africa: List<String> = emptyList(),
    @Json(name = "asia")
    val asia: List<String> = emptyList(),
    @Json(name = "latin-america-caribbean")
    val latam: List<String> = emptyList(),
    @Json(name = "oceania")
    val oceania: List<String> = emptyList(),
    @Json(name = "europe")
    val europe: List<String> = emptyList()
) {
    companion object {
        val EMPTY = CountryListDTO(
            africa = emptyList(),
            asia = emptyList(),
            latam = emptyList(),
            oceania = emptyList(),
            europe = emptyList()
        )
    }
}
