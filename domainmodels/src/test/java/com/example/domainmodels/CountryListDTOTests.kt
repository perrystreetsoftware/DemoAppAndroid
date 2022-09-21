package com.example.domainmodels
import com.perrystreet.testutils.readJsonToString
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryListDTOTests {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .build()
    }

    lateinit var countryListDto: CountryListDTO

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