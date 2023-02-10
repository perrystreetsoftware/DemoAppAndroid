package com.example.feature.countrylist.error

import com.example.AutoCloseKoinAfterEachExtension
import com.example.errors.CountryListError
import com.example.interfaces.networkLogicApiMocks
import com.example.logic.logicModule
import com.example.repositories.repositoriesModule
import com.example.uicomponents.models.FloatingAlert
import com.example.viewmodels.CountryListViewModel
import com.example.viewmodels.viewModelModule
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(AutoCloseKoinAfterEachExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class CountryListErrorDialogFactoryTest : KoinTest {
    private val dialogErrors = listOf(
        CountryListError.Forbidden,
        CountryListError.ConnectionError,
        CountryListError.Other,
        CountryListError.BlockedCountry("reason")
    )

    @BeforeEach
    open fun setup() {
        startKoin {
            loadKoinModules(viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks)
        }
    }

    private val viewModel: CountryListViewModel by inject()

    @Test
    fun `Blocked dialog has positive action`() {
        val state = CountryListError.BlockedCountry("reason").asFloatingAlert(viewModel) {}

        (state as FloatingAlert.Dialog).let {
            it.state.positiveAction.onClick.shouldNotBeNull()
            it.state.negativeAction?.onClick.shouldBeNull()
        }
    }

    @Test
    fun `Forbidden, Connection do not have custom actions`() {
        val forbidden = CountryListError.Forbidden.asFloatingAlert(viewModel) {}
        val connection = CountryListError.ConnectionError.asFloatingAlert(viewModel) {}

        listOf(forbidden, connection).forEach { state ->
            (state as FloatingAlert.Dialog).apply {
                this.state.positiveAction.onClick.shouldBeNull()
                this.state.negativeAction?.onClick.shouldBeNull()
            }
        }
    }

    @Test
    fun `Return dialog state for supported errors`() {
        dialogErrors.forEach { error ->
            val alert = error.asFloatingAlert(viewModel) {}

            (alert as FloatingAlert.Dialog).apply {
                this.shouldNotBeNull()
            }
        }
    }
}