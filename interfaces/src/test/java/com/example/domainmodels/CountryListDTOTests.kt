package com.example.domainmodels

import com.example.dtos.CountryListDTO
import com.example.readJsonToString
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldBeGreaterThan
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryListDTOTests {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .build()
    }

    private lateinit var countryListDto: CountryListDTO

    @BeforeEach
    fun setup() {
        countryListDto = moshi.adapter(CountryListDTO::class.java)
            .fromJson(readJsonToString("country_list_dto.json"))!!
    }

    @Test
    fun `then it has parsed`() {
        countryListDto.asia.count().shouldBeGreaterThan(0)
        countryListDto.africa.count().shouldBeGreaterThan(0)
        countryListDto.oceania.count().shouldBeGreaterThan(0)
        countryListDto.latam.count().shouldBeGreaterThan(0)
        countryListDto.europe.count().shouldBeGreaterThan(0)
    }
}
