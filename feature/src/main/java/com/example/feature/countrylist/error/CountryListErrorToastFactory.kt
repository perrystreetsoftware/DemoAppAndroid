package com.example.feature.countrylist.error

import android.content.Context
import com.example.errors.CountryListError
import com.example.features.R
import com.example.uicomponents.error.ErrorToastFactory
import com.example.uicomponents.models.ToastState


class CountryListErrorToastFactory(private val context: Context) : ErrorToastFactory<CountryListError> {
    override fun getToastState(error: CountryListError): ToastState? = when (error) {
        CountryListError.NotAvailableError -> ToastState(context.getString(R.string.country_list_unavailable_error))
        else -> null
    }
}

fun CountryListError.asToastState(context: Context): ToastState? =
    CountryListErrorToastFactory(context).getToastState(this)



