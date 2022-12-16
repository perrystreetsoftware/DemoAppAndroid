package com.example.feature.countrylist.error

import com.example.errors.CountryListError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CountryListErrorMapperTest {

    private val mapper = CountryListErrorMapper

    @Test
    fun `ConnectionError maps to dialog error`() {
        Assertions.assertEquals(CountryListDialogError.Connection, mapper.toUiError(CountryListError.ConnectionError))
    }

    @Test
    fun `Forbidden maps to dialog error`() {
        Assertions.assertEquals(CountryListDialogError.Forbidden, mapper.toUiError(CountryListError.Forbidden))
    }

    @Test
    fun `BlockedCountry maps to dialog error`() {
        Assertions.assertEquals(CountryListDialogError.Blocked("reason"),
            mapper.toUiError(CountryListError.BlockedCountry("reason")))
    }

    @Test
    fun `Other and UserNotLogged in map to generic dialog error`() {
        val other = mapper.toUiError(CountryListError.Other)
        val notLoggedIn = mapper.toUiError(CountryListError.UserNotLoggedIn)
        val expected = CountryListDialogError.Generic
        Assertions.assertEquals(expected, other)
        Assertions.assertEquals(expected, notLoggedIn)
    }

    @Test
    fun `NotAvailableError maps to Toast error`() {
        Assertions.assertEquals(CountryListToastError.NotAvailable,
            mapper.toUiError(CountryListError.NotAvailableError))
    }

    @Test
    fun `NotEnoughPermissionsError maps to Banner error`() {
        Assertions.assertEquals(CountryListBannerError.NoPermissions,
            mapper.toUiError(CountryListError.NotEnoughPermissionsError))
    }

    @Test
    fun `InternalError maps to null`() {
        Assertions.assertNull(mapper.toUiError(CountryListError.InternalError))
    }


}