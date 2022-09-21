package com.example.repositories

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.CountryDetailsDTO
import com.example.interfaces.MockTravelApi
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.networkLogicApiMocks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.lang.RuntimeException

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsRepositoryTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(repositoriesModule + networkLogicApiMocks)
        }
    }
    val countryDetailsRepository: CountryDetailsRepository by inject()
    val api: ITravelAdvisoriesApi by inject()

    @Nested
    @DisplayName("#getDetails")
    inner class GetDetails {
        lateinit var testObserver: TestObserver<CountryDetailsDTO>
        lateinit var value: CountryDetailsDTO

        @Nested
        @DisplayName("when success")
        inner class Success {

            @BeforeEach
            fun setup() {
                testObserver = countryDetailsRepository.getDetails("ug").test()
                value = testObserver.values().first()
            }

            @Test
            fun `then it should be valid`() {
                value.shouldBeEqualTo(CountryDetailsDTO(area = "Asia", regionName = "Yemen", regionCode = "YE", legalCodeBody = "Article 264"))
            }
        }

        @Nested
        @DisplayName("when failure")
        inner class Failure {
            val error = RuntimeException("test")

            @BeforeEach
            fun setup() {
                (api as MockTravelApi).getCountryDetailsResult = Observable.error(error)
                testObserver = countryDetailsRepository.getDetails("ug").test()
            }

            @Test
            fun `then it should not be valid`() {
                testObserver.assertError(error)
            }
        }
    }
}