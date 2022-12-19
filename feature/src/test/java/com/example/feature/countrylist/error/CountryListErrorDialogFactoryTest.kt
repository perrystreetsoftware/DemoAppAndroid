package com.example.feature.countrylist.error

import com.example.errors.CountryListError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CountryListErrorDialogFactoryTest {

    private val goToRandom: () -> Unit = {}

    private val dialogErrors = listOf(CountryListError.Forbidden,
        CountryListError.ConnectionError,
        CountryListError.Other,
        CountryListError.BlockedCountry("reason"))


    private val factory = CountryListErrorDialogFactory(goToRandom)

    @Test
    fun `Blocked dialog has positive action`() {
        val state = factory.getDialogState(CountryListError.BlockedCountry("reason"))

        Assertions.assertEquals(state?.dialogActions?.onPositive, goToRandom)
        Assertions.assertNull(state?.dialogActions?.onNegative)
    }

    @Test
    fun `Forbidden, Connection and Generic do not have custom actions`() {
        val forbidden = factory.getDialogState(CountryListError.Forbidden)
        val connection = factory.getDialogState(CountryListError.ConnectionError)
        val generic = factory.getDialogState(CountryListError.Other)

        listOf(forbidden, connection, generic).forEach { state ->
            Assertions.assertNull(state?.dialogActions?.onPositive)
            Assertions.assertNull(state?.dialogActions?.onNegative)
        }
    }

    @Test
    fun `Return dialog state for supported errors`() {
        dialogErrors.forEach { error ->
            Assertions.assertNotNull(factory.getDialogState(error))
        }
    }

}