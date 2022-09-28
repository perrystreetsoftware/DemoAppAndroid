package com.example.repositories

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.Continent
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.networkLogicApiMocks
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountrySelectingLogicTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(repositoriesModule + networkLogicApiMocks)
        }
    }
    val countrySelectingRepository: CountrySelectingPushBasedRepository by inject()
    val api: ITravelAdvisoriesApi by inject()

    @Nested
    @DisplayName("#init")
    inner class GetDetails {
        lateinit var testObserver: TestObserver<List<Continent>>
        lateinit var value: List<Continent>

        @BeforeEach
        fun setup() {
            testObserver = countrySelectingRepository.continents.test()
            value = testObserver.values().last()
        }

        @Test
        fun `then it should be empty`() {
            value.shouldBeEmpty()
        }

        @Nested
        @DisplayName("#reload")
        inner class Reload {
            lateinit var reloadObserver: TestObserver<Void>

            @BeforeEach
            fun setup() {
                reloadObserver = countrySelectingRepository.reload().test()
                testObserver.awaitCount(2)
                value = testObserver.values().last()
            }

            @Test
            fun `then it should be valid`() {
                value.count().shouldBeEqualTo(5)
            }
        }
    }
}