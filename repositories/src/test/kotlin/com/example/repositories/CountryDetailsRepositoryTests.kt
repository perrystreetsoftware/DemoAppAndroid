package com.example.repositories

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.errors.CountryDetailsError
import com.example.interfaces.MockTravelApi
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.networkLogicApiMocks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsRepositoryTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(repositoriesModule + networkLogicApiMocks)
        }
    }
    val countryDetailsRepository: CountryDetailsPullBasedRepository by inject()
    val api: ITravelAdvisoriesApi by inject()

    @Nested
    @DisplayName("#getDetails")
    inner class GetDetails {
        lateinit var testObserver: TestObserver<CountryDetails>
        lateinit var value: CountryDetails

        @Nested
        @DisplayName("when success")
        inner class Success {

            @BeforeEach
            fun setup() {
                testObserver = countryDetailsRepository.getDetails("ug").test().awaitDone(5, TimeUnit.SECONDS)
                value = testObserver.values().first()
            }

            @Test
            fun `then it should be valid`() {
                value.shouldBeEqualTo(
                    CountryDetails(country = Country(regionCode = "YE"), detailsText = "Article 264")
                )
            }
        }

        @Nested
        @DisplayName("when failure")
        inner class Failure {
            private val error = RuntimeException("test")

            @BeforeEach
            fun setup() {
                (api as MockTravelApi).getCountryDetailsResult = Single.error(error)
                testObserver = countryDetailsRepository.getDetails("ug").test()
            }

            @Test
            fun `then it should not be valid`() {
                testObserver.assertError(CountryDetailsError.Other(error))
            }
        }
    }
}