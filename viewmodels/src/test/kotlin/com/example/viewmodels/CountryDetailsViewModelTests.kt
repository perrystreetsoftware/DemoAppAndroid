package com.example.viewmodels

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.Country
import com.example.domainmodels.CountryDetails
import com.example.interfaces.networkLogicApiMocks
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsViewModelTests : KoinTest {
    @BeforeEach
    fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }

        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        stateTestObserver = viewModel.state.test()
    }

    private val country = Country(regionCode = "ug")
    private val viewModel: CountryDetailsViewModel by inject() {
        parametersOf(country.regionCode)
    }

    lateinit var stateTestObserver: TestObserver<CountryDetailsViewModel.UiState>
    private lateinit var testScheduler: TestScheduler

    @AfterEach
    fun cleanup() {
        RxJavaPlugins.setComputationSchedulerHandler(null)
    }

    @Test
    fun `then it starts having transitioned to loading`() {
        stateTestObserver.values().shouldBeEqualTo(listOf(CountryDetailsViewModel.UiState.Loading))
    }

    @Nested
    @DisplayName("#state")
    inner class Advance {
        private lateinit var state: CountryDetailsViewModel.UiState

        @BeforeEach
        fun setup() {
            testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
            stateTestObserver.awaitCount(2)
            state = stateTestObserver.values().last()
        }

        @Test
        fun `then it transitions to loaded`() {
            stateTestObserver.values().count().shouldBeEqualTo(2)
        }

        @Test
        fun `then it has loaded content`() {
            state.shouldBeEqualTo(
                CountryDetailsViewModel.UiState.Loaded(
                    CountryDetails(
                        Country(regionCode = "YE"),
                        detailsText = "Article 264"
                    )
                )
            )
        }
    }
}
