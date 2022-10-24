package com.example.viewmodels

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.CountryDetails
import com.example.domainmodels.Country
import com.example.interfaces.networkLogicApiMocks
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountryDetailsViewModelTests: KoinTest {
    @BeforeEach
    fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }
    private val country = Country(regionCode = "ug")
    val viewModel: CountryDetailsViewModel by inject() {
        parametersOf(country)
    }

    @Nested
    @DisplayName("#onPageLoaded")
    inner class OnAppear {
        lateinit var stateTestObserver: TestObserver<CountryDetailsViewModel.State>
        private lateinit var testScheduler: TestScheduler

        @BeforeEach
        fun `setup`() {
            testScheduler = TestScheduler()
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

            stateTestObserver = viewModel.state.test()
        }

        @AfterEach
        fun cleanup() {
            RxJavaPlugins.setComputationSchedulerHandler(null)
        }

        @Test
        fun `then it starts with init`() {
            stateTestObserver.values().shouldBeEqualTo(listOf(CountryDetailsViewModel.State.Initial))
        }

        @Nested
        @DisplayName("onPageLoaded")
        inner class OnPageLoaded {
            @BeforeEach
            fun `setup`() {
                viewModel.onPageLoaded(regionCode = country.regionCode)
            }

            @Test
            fun `then it transitions to loading`() {
                stateTestObserver.values().shouldBeEqualTo(
                    listOf(
                        CountryDetailsViewModel.State.Initial,
                        CountryDetailsViewModel.State.Loading
                    )
                )
            }

            @Nested
            @DisplayName("When I advance")
            inner class Advance {
                @BeforeEach
                fun `setup`() {
                    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
                }

                @Test
                fun `then it has loaded`() {
                    stateTestObserver.values().shouldBeEqualTo(
                        listOf(
                            CountryDetailsViewModel.State.Initial,
                            CountryDetailsViewModel.State.Loading,
                            CountryDetailsViewModel.State.Loaded(
                                CountryDetails(
                                    Country(regionCode = "YE"),
                                    detailsText = "Article 264"
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}