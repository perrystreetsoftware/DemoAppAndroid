package com.example.viewmodels.error.countrylist

sealed class CountryListUiError

sealed class CountryListDialogError : CountryListUiError() {
    object Forbidden : CountryListDialogError()
    object Connection : CountryListDialogError()
    data class Blocked(val reason: String) : CountryListDialogError()
    object Generic : CountryListDialogError()
}

sealed class CountryListToastError : CountryListUiError() {
    object NotAvailable : CountryListToastError()
}

sealed class CountryListBannerError : CountryListUiError() {
    object NoPermissions : CountryListBannerError()
}