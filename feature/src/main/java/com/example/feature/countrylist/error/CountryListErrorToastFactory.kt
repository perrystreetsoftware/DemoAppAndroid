package com.example.feature.countrylist.error

import com.example.features.R
import com.example.uicomponents.error.ErrorToastFactory


object CountryListErrorToastFactory : ErrorToastFactory<CountryListToastError> {
    override fun getToastMessage(error: CountryListToastError): Int = when (error) {
        CountryListToastError.NotAvailable -> R.string.country_list_unavailable_error
    }
}

fun CountryListToastError.toastMessage(): Int = CountryListErrorToastFactory.getToastMessage(this)



