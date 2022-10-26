package com.example.domainmodels
import com.example.dtos.CountryDetailsDTO
import com.perrystreet.testutils.readJsonToString
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsDTOTests {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .build()
    }

    lateinit var countryDetailsDto: CountryDetailsDTO

    @BeforeEach
    fun setup() {
        countryDetailsDto = moshi.adapter(CountryDetailsDTO::class.java)
            .fromJson(readJsonToString("country_details_dto.json"))!!
    }

    @Test
    fun `then it has parsed`() {
        countryDetailsDto.area.shouldNotBeNull()
        countryDetailsDto.regionCode.shouldNotBeNull()
        countryDetailsDto.regionName.shouldNotBeNull()
        countryDetailsDto.legalCodeBody.shouldNotBeNull()
    }
}