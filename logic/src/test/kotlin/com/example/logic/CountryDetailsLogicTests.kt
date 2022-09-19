package com.example.repositories

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.CountryDetailsUIModel
import com.example.domainmodels.CountryUIModel
import com.example.interfaces.networkLogicApiMocks
import com.example.logic.CountryDetailsLogic
import com.example.logic.logicModule
import io.reactivex.rxjava3.observers.TestObserver
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsLogicTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }
    val logic: CountryDetailsLogic by inject()

    @Nested
    @DisplayName("#getDetails")
    inner class GetDetails {
        lateinit var testObserver: TestObserver<CountryDetailsUIModel>
        lateinit var value: CountryDetailsUIModel
        val country = CountryUIModel(countryName = "Uganda", regionCode = "ug")

        @Nested
        @DisplayName("when success")
        inner class Success {
            @BeforeEach
            fun setup() {
                testObserver = logic.getDetails(country).test()
                value = testObserver.values().first()
            }

            @Test
            fun `then it should be valid`() {
                value.shouldBeEqualTo(CountryDetailsUIModel(countryName = "Yemen", regionCode = "YE", detailsText = "Article 264"))
            }
        }
    }
}