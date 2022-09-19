package com.example.logic

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.CountryDetailsUIModel
import com.example.domainmodels.CountryUIModel
import com.example.interfaces.networkLogicApiMocks
import com.example.repositories.repositoriesModule
import com.example.viewmodels.CountryDetailsViewModel
import com.example.viewmodels.CountrySelectingViewModel
import com.example.viewmodels.viewModelModule
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
    open fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }
    private val country = CountryUIModel(countryName = "Uganda", regionCode = "ug")
    val viewModel: CountryDetailsViewModel by inject() {
        parametersOf(country)
    }

    @Nested
    @DisplayName("#onAppear")
    inner class OnAppear {
        lateinit var stateTestObserver: TestObserver<CountryDetailsViewModel.State>
        lateinit var detailsTestObserver: TestObserver<CountryDetailsUIModel>
        private lateinit var testScheduler: TestScheduler

        @BeforeEach
        fun `setup`() {
            testScheduler = TestScheduler()
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

            stateTestObserver = viewModel.state.test()
            detailsTestObserver = viewModel.details.test()
            viewModel.onAppear()
        }

        @AfterEach
        fun cleanup() {
            RxJavaPlugins.setComputationSchedulerHandler(null)
        }


        @Test
        fun `then it transitions to loading`() {
            stateTestObserver.values().shouldBeEqualTo(listOf(CountryDetailsViewModel.State.Initial, CountryDetailsViewModel.State.Loading))
        }

        @Test
        fun `then it has not loaded`() {
            detailsTestObserver.values().last().shouldBeEqualTo(CountryDetailsUIModel.EMPTY)
        }

        @Nested
        @DisplayName("When I advance")
        inner class Advance {
            @BeforeEach
            fun `setup`() {
                testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
                detailsTestObserver.awaitCount(2)
            }

            @Test
            fun `then it has loaded`() {
                stateTestObserver.values().shouldBeEqualTo(listOf(CountryDetailsViewModel.State.Initial, CountryDetailsViewModel.State.Loading, CountryDetailsViewModel.State.Initial))
            }

            @Test
            fun `then it has loaded values`() {
                detailsTestObserver.values().last().shouldBeEqualTo(CountryDetailsUIModel(regionCode = "YE", countryName = "Yemen", detailsText = "Article 264"))
            }
        }
    }
}