package com.example.domainmodels
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryDetailsDTO(
    @Json(name="area")
    val area: String,
    @Json(name="region_name")
    val regionName: String,
    @Json(name="iso_2")
    val regionCode: String,
    @Json(name="legal_code_body")
    val legalCodeBody: String
) {
    companion object {
        val EMPTY = CountryDetailsDTO(area = "", regionName = "", regionCode = "", legalCodeBody = "")
    }
}