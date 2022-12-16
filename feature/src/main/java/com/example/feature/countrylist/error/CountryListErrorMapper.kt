package com.example.feature.countrylist.error

import com.example.errors.CountryListError
import com.example.uicomponents.error.UiErrorMapper

object CountryListErrorMapper : UiErrorMapper<CountryListError, CountryListUiError> {

    override fun toUiError(error: CountryListError): CountryListUiError? = when (error) {
        CountryListError.ConnectionError -> CountryListDialogError.Connection
        CountryListError.Forbidden -> CountryListDialogError.Forbidden
        CountryListError.Other -> CountryListDialogError.Generic
        CountryListError.UserNotLoggedIn -> CountryListDialogError.Generic
        CountryListError.NotAvailableError -> CountryListToastError.NotAvailable
        CountryListError.NotEnoughPermissionsError -> CountryListBannerError.NoPermissions
        is CountryListError.BlockedCountry -> CountryListDialogError.Blocked(reason = error.reason)
        CountryListError.InternalError -> null // Not all logic errors might have a UI error equivalent
        // Avoid using else clause, this way the compiler will make sure you consider the mapper when adding new domain errors
    }
}

fun CountryListError.toUiError(): CountryListUiError? = CountryListErrorMapper.toUiError(this)