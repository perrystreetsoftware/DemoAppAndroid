package com.example.feature.countrylist.error

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CountryListErrorDialogFactoryTest {

    private val goToRandom: () -> Unit = {}


    private val factory = CountryListErrorDialogFactory(goToRandom)


    @Test
    fun `Blocked dialog has positive action`() {
        val state = factory.getDialogState(CountryListDialogError.Blocked)

        Assertions.assertEquals(state.dialogActions.onPositive, goToRandom)
        Assertions.assertNull(state.dialogActions.onNegative)
    }

    @Test
    fun `Forbidden, Connection and Generic do not have custom actions`() {
        val forbidden = factory.getDialogState(CountryListDialogError.Forbidden)
        val connection = factory.getDialogState(CountryListDialogError.Connection)
        val generic = factory.getDialogState(CountryListDialogError.Generic)

        listOf(forbidden, connection, generic).forEach { state ->
            Assertions.assertNull(state.dialogActions.onPositive)
            Assertions.assertNull(state.dialogActions.onNegative)
        }

    }

}