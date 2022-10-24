package com.example.viewmodels

import com.example.AutoCloseKoinAfterEachExtension
import com.example.domainmodels.ServerStatus
import com.example.interfaces.networkLogicApiMocks
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
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
    fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }

        testScheduler = TestScheduler()
        stateTestObserver = viewModel.state.test()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @AfterEach
    fun `cleanup`() {
        RxJavaPlugins.setComputationSchedulerHandler(null)
    }

    val viewModel: CountrySelectingViewModel by inject()
    private lateinit var testScheduler: TestScheduler
    lateinit var stateTestObserver: TestObserver<CountrySelectingViewModel.UiState>

    @Test
    fun `then it transitions to loading`() {
        stateTestObserver.values().shouldBeEqualTo(listOf(CountrySelectingViewModel.UiState()))
    }

    @Nested
    @DisplayName("#onPageLoaded")
    inner class OnPageAppear {
        @BeforeEach
        fun `setup`() {
            viewModel.onPageLoaded()
        }

        @Test
        fun `then it transitions to loading`() {
            stateTestObserver.values().last().shouldBeEqualTo(CountrySelectingViewModel.UiState(isLoading = true, serverStatus = ServerStatus.EMPTY))
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
                stateTestObserver.values().last().continents.isNotEmpty().shouldBeTrue()
            }
        }
    }
}