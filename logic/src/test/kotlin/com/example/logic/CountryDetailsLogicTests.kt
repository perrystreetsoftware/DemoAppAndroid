package com.example.logic

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
import com.example.interfaces.networkLogicApiMocks
import com.example.repositories.repositoriesModule
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsLogicTests : KoinTest {
    @BeforeEach
    fun setup() {
        startKoin {
            loadKoinModules(logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }

    val logic: CountryDetailsLogic by inject()

    @Nested
    @DisplayName("#getDetails")
    inner class GetDetails {
        lateinit var testObserver: TestObserver<CountryDetails>
        lateinit var value: CountryDetails
        val country = Country(regionCode = "ug")

        @Nested
        @DisplayName("when success")
        inner class Success {
            @BeforeEach
            fun setup() {
                testObserver = logic.getDetails(country.regionCode).test()
                testObserver.awaitCount(1)
                value = testObserver.values().first()
            }

            @Test
            fun `then it should be valid`() {
                value.shouldBeEqualTo(
                    CountryDetails(
                        country = Country(regionCode = "YE"),
                        detailsText = "Article 264"
                    )
                )
            }
        }
    }
}
