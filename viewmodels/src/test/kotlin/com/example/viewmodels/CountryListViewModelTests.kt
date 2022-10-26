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
import org.amshove.kluent.shouldBeFalse
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
class CountryListViewModelTests: KoinTest {
    @BeforeEach
    fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }

        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        stateTestObserver = viewModel.state.test()
    }

    @AfterEach
    fun `cleanup`() {
        RxJavaPlugins.setComputationSchedulerHandler(null)
    }

    private val viewModel: CountryListViewModel by inject()
    private lateinit var testScheduler: TestScheduler
    lateinit var stateTestObserver: TestObserver<CountryListViewModel.UiState>

    @Test
    fun `then it startings having transitioned to loading`() {
        stateTestObserver.values().shouldBeEqualTo(listOf(
            CountryListViewModel.UiState(isLoading = true, serverStatus = ServerStatus.EMPTY)
        ))
    }

    @Nested
    @DisplayName("when I advance")
    inner class Advance {
        @BeforeEach
        fun `setup`() {
            testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        }

        @Test
        fun `then the middle emission is still loading`() {
            stateTestObserver.values()[1].apply {
                isLoaded.shouldBeFalse()
                isLoading.shouldBeTrue()
                continents.isNotEmpty().shouldBeTrue()
            }
        }

        @Test
        fun `then the final emission has loaded`() {
            stateTestObserver.values()[2].apply {
                isLoaded.shouldBeTrue()
                isLoading.shouldBeFalse()
                continents.isNotEmpty().shouldBeTrue()
            }
        }
    }
}