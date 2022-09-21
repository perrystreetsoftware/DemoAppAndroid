package com.example.logic

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.ContinentUIModel
import com.example.interfaces.networkLogicApiMocks
import com.example.repositories.repositoriesModule
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
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.TimeUnit

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CountrySelectingViewModelTests: KoinTest {
    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }
    val viewModel: CountrySelectingViewModel by inject()

    @Nested
    @DisplayName("#onPageLoaded")
    inner class OnAppear {
        lateinit var stateTestObserver: TestObserver<CountrySelectingViewModel.State>
        lateinit var continentsTestObserver: TestObserver<List<ContinentUIModel>>
        private lateinit var testScheduler: TestScheduler

        @BeforeEach
        fun `setup`() {
            testScheduler = TestScheduler()
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

            stateTestObserver = viewModel.state.test()
            continentsTestObserver = viewModel.continents.test()
            viewModel.onPageLoaded()
        }

        @AfterEach
        fun cleanup() {
            RxJavaPlugins.setComputationSchedulerHandler(null)
        }


        @Test
        fun `then it transitions to loading`() {
            stateTestObserver.values().shouldBeEqualTo(listOf(CountrySelectingViewModel.State.Initial, CountrySelectingViewModel.State.Loading))
        }

        @Test
        fun `then it has not loaded`() {
            continentsTestObserver.values().last().shouldBeEqualTo(emptyList())
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
                stateTestObserver.values().shouldBeEqualTo(listOf(CountrySelectingViewModel.State.Initial, CountrySelectingViewModel.State.Loading, CountrySelectingViewModel.State.Initial))
            }

            @Test
            fun `then it has loaded values`() {
                continentsTestObserver.values().last().count().shouldBeEqualTo(5)
            }
        }
    }
}