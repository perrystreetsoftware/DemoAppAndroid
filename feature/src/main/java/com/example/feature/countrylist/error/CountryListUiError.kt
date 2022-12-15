package com.example.feature.countrylist.error

sealed class CountryListUiError

sealed class CountryListDialogError : CountryListUiError() {
    object Forbidden : CountryListDialogError()
    object Connection : CountryListDialogError()
    object Blocked : CountryListDialogError()
    object Generic : CountryListDialogError()
}

sealed class CountryListToastError : CountryListUiError() {
    object NotAvailable : CountryListToastError()
}

sealed class CountryListBannerError : CountryListUiError() {
    object NoPermissions : CountryListBannerError()
}